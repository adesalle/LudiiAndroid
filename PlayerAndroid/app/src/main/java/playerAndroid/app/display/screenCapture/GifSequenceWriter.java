package playerAndroid.app.display.screenCapture;


import android.graphics.Bitmap;

import java.io.IOException;
import java.util.Iterator;

import androidUtils.imageio.IIOImage;
import androidUtils.imageio.ImageWriter;
import  androidUtils.imageio.exceptions.IIOException;


import androidUtils.imageio.ImageIO;
import androidUtils.imageio.ImageOutputStream;
import androidUtils.awt.image.RenderedImage;
import androidUtils.imageio.ImageTypeSpecifier;
import androidUtils.imageio.ImageWriteParam;
import androidUtils.imageio.metadata.IIOMetadata;
import androidUtils.imageio.metadata.IIOMetadataNode;


public class GifSequenceWriter {
    protected ImageWriter gifWriter;
    protected ImageWriteParam imageWriteParam;
    protected IIOMetadata imageMetaData;

    /**
    * Creates a new GifSequenceWriter
    *
    * @param outputStream the ImageOutputStream to be written to
    * @param imageType one of the imageTypes specified in BufferedImage
    * @param timeBetweenFramesMS the time between frames in milliseconds
    * @param loopContinuously whether the gif should loop repeatedly
    * @throws IOException if no gif ImageWriters are found
    *
    * @author Elliot Kroo (elliot[at]kroo[dot]net)
    */
    public GifSequenceWriter(
      final ImageOutputStream outputStream,
      final int imageType,
      final int timeBetweenFramesMS,
      final boolean loopContinuously) throws IOException {
    // my method to create a writer
    gifWriter = getWriter();
    imageWriteParam = gifWriter.getDefaultWriteParam();
    final ImageTypeSpecifier imageTypeSpecifier =
      ImageTypeSpecifier.createFromBufferedImageType(imageType);

    imageMetaData =
      gifWriter.getDefaultImageMetadata(imageTypeSpecifier,
      imageWriteParam);

    final String metaFormatName = imageMetaData.getNativeMetadataFormatName();

    final IIOMetadataNode root = (IIOMetadataNode)
      imageMetaData.getAsTree(metaFormatName);

    final IIOMetadataNode graphicsControlExtensionNode = getNode(
      root,
      "GraphicControlExtension");

    graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
    graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
    graphicsControlExtensionNode.setAttribute(
      "transparentColorFlag",
      "FALSE");
    graphicsControlExtensionNode.setAttribute(
      "delayTime",
      Integer.toString(timeBetweenFramesMS / 10));
    graphicsControlExtensionNode.setAttribute(
      "transparentColorIndex",
      "0");

    final IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
    commentsNode.setAttribute("CommentExtension", "Created by MAH");

    final IIOMetadataNode appEntensionsNode = getNode(
      root,
      "ApplicationExtensions");

    final IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");

    child.setAttribute("applicationID", "NETSCAPE");
    child.setAttribute("authenticationCode", "2.0");

    final int loop = loopContinuously ? 0 : 1;

    child.setUserObject(new byte[]{ 0x1, (byte) (loop & 0xFF), (byte)
      ((loop >> 8) & 0xFF)});
    appEntensionsNode.appendChild(child);

    imageMetaData.setFromTree(metaFormatName, root);

    gifWriter.setOutput(outputStream);

    gifWriter.prepareWriteSequence(null);
    }

    public void writeToSequence(final RenderedImage img) throws IOException {
    gifWriter.writeToSequence(
      new IIOImage( img,
              null,
        imageMetaData),
      imageWriteParam);
    }

    /**
    * Close this GifSequenceWriter object. This does not close the underlying
    * stream, just finishes off the GIF.
    */
    public void close() throws IOException {
    gifWriter.endWriteSequence();
    }

    /**
    * Returns the first available GIF ImageWriter using
    * ImageIO.getImageWritersBySuffix("gif").
    *
    * @return a GIF ImageWriter object
    * @throws IIOException if no GIF image writers are returned
    */
    private static ImageWriter getWriter() throws IIOException {
    final Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
    if(!iter.hasNext()) {
      throw new IIOException("No GIF Image Writers Exist");
    } else {
      return iter.next();
    }
    }

    /**
    * Returns an existing child node, or creates and returns a new child node (if
    * the requested node does not exist).
    *
    * @param rootNode the <tt>IIOMetadataNode</tt> to search for the child node.
    * @param nodeName the name of the child node.
    *
    * @return the child node, if found or a new node created with the given name.
    */
    private static IIOMetadataNode getNode(
      final IIOMetadataNode rootNode,
      final String nodeName) {
    final int nNodes = rootNode.getLength();
    for (int i = 0; i < nNodes; i++) {
      if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName)
          == 0) {
        return((IIOMetadataNode) rootNode.item(i));
      }
    }
    final IIOMetadataNode node = new IIOMetadataNode(nodeName);
    rootNode.appendChild(node);
    return(node);
    }

}