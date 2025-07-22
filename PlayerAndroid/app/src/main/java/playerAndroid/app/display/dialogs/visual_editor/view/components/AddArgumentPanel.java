package playerAndroid.app.display.dialogs.visual_editor.view.components;

import androidUtils.awt.BoxLayout;
import androidUtils.awt.Dimension;
import androidUtils.awt.Font;
import androidUtils.awt.Point;
import androidUtils.awt.event.MouseAdapter;
import androidUtils.awt.event.MouseEvent;
import androidUtils.awt.event.MouseListener;
import androidUtils.swing.DefaultListModel;
import androidUtils.swing.JList;
import androidUtils.swing.JPanel;

import androidUtils.swing.JScrollPane;
import androidUtils.swing.JTextField;
import androidUtils.swing.event.DocumentEvent;
import androidUtils.swing.event.DocumentListener;
import main.grammar.Clause;
import main.grammar.ClauseArg;
import main.grammar.Symbol;
import playerAndroid.app.display.dialogs.visual_editor.documentation.DocumentationReader;
import playerAndroid.app.display.dialogs.visual_editor.handler.Handler;
import playerAndroid.app.display.dialogs.visual_editor.model.LudemeNode;
import playerAndroid.app.display.dialogs.visual_editor.model.NodeArgument;
import playerAndroid.app.display.dialogs.visual_editor.recs.utils.ReadableSymbol;
import playerAndroid.app.display.dialogs.visual_editor.view.components.ludemenodecomponent.LudemeNodeComponent;
import playerAndroid.app.display.dialogs.visual_editor.view.components.ludemenodecomponent.inputs.LInputField;
import playerAndroid.app.display.dialogs.visual_editor.view.panels.IGraphPanel;


import java.util.ArrayList;
import java.util.List;

public class AddArgumentPanel extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2621063598425008191L;
	final DefaultListModel<ReadableSymbol> listModel = new DefaultListModel<ReadableSymbol>();
    final JList<ReadableSymbol> list = new JList<ReadableSymbol>(listModel)
    {

        @Override
        public String getToolTipText(MouseEvent e)
        {
            String htmlTip = "<html>";
            int index = locationToIndex(e.getPoint());
            if(index == -1)
                return null;
            String description = null;
            String remark = null;
            try
            {
                description = DocumentationReader.instance().documentation().get((listModel.getElementAt(index).getSymbol())).description();
                remark = DocumentationReader.instance().documentation().get((listModel.getElementAt(index).getSymbol())).remark();
            }
            catch(Exception ignored) 
            {
            	// Nothing to do
            }

            Font fontMetrics = searchField.getFontMetrics(searchField.getFont());

            htmlTip += description.replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "<br>";
            if(remark != null)
            {
                int length = fontMetrics.stringWidth(remark);
                htmlTip += "<p width=\"" + (Math.min(length, 350)) + "px\">" + remark.replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</p>";

            }
            htmlTip += "</html>";

            return htmlTip;
        }

        @Override
        public Point getToolTipLocation(MouseEvent e)
        {
            return new Point(e.getX() + 10, e.getY() + 10);
        }
    };
    final JScrollPane scrollableList = new JScrollPane(list);
    public final JTextField searchField = new JTextField();
    LInputField initiator;
    List<ReadableSymbol> currentSymbols;
    public AddArgumentPanel(List<Symbol> symbolList, IGraphPanel graphPanel, boolean connect)
    {
        this(symbolList, graphPanel, connect, false);
    }

    public AddArgumentPanel(List<Symbol> symbolList, IGraphPanel graphPanel, boolean connect, boolean addsDefines)
    {
        MouseListener mouseListener = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int index = list.locationToIndex(e.getPoint());
                if (index == -1)
                    return;

                Symbol s = listModel.getElementAt(index).getSymbol();

                // if adds define
                if (addsDefines)
                {
                    // find corresponding node
                    for (LudemeNode n : defineNodes)
                    {
                        if (n.symbol() == s)
                        {
                            n.setX(getLocation().x);
                            n.setY(getLocation().y);
                            Handler.addNode(graphPanel.graph(), n);
                            break;
                        }
                    }
                }
                // if the symbol is a terminal, do not create a new node. Instead create a new InputField
                else if (s.ludemeType().equals(Symbol.LudemeType.Predefined) || isConstantTerminal(s)) {
                    // get the node that we are adding an argument to
                    LudemeNodeComponent lnc = graphPanel.connectionHandler().selectedComponent().lnc();
                    lnc.addTerminal(s);
                    graphPanel.connectionHandler().cancelNewConnection();
                    setVisible(false);
                }
                // otherwise if it's a ludeme, create a new node
                else
                {
                    // Find the matching NodeArgument of the initiator InputField for the chosen symbol, if initiator is not null
                    if (initiator != null)
                    {
                        NodeArgument match = null;
                        for (NodeArgument na : initiator.nodeArguments())
                        {
                            if (na.possibleSymbolInputsExpanded().contains(s))
                            {
                                match = na;
                                break;
                            }
                        }
                        Handler.addNode(graphPanel.graph(), s, match, getLocation().x, getLocation().y, connect);
                    }
                    else
                    {
                        Handler.addNode(graphPanel.graph(), s, null, getLocation().x, getLocation().y, connect);
                    }
                }
                searchField.setText("");
                scrollableList.getVerticalScrollBar().setValue(0);
                setVisible(false);
            }
        };
        list.addMouseListener(mouseListener);

        updateList(null, symbolList);

        DocumentListener searchListener = new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                listModel.clear();
                for (ReadableSymbol rs : currentSymbols)
                {
                    if (rs.getSymbol().name().toLowerCase().contains(searchField.getText().toString().toLowerCase()) || rs.getSymbol().token().toLowerCase().contains(searchField.getText().toString().toLowerCase())
                            || rs.getSymbol().grammarLabel().toLowerCase().contains(searchField.getText().toString().toLowerCase()))
                        listModel.addElement(rs);
                }
                repaint();
            }
        };
        searchField.getDocument().addDocumentListener(searchListener);
    }

    public void updateList(LInputField initiator1, List<Symbol> symbolList)
    {
        this.initiator = initiator1;
        // remove duplicates
        List<Symbol> symbolList2 = symbolList;
        symbolList2 = symbolList.stream().distinct().collect(java.util.stream.Collectors.toList());
        currentSymbols = new ArrayList<>();
        searchField.setText("");
        listModel.clear();
        for(Symbol symbol : symbolList2)
        {
            ReadableSymbol rs = new ReadableSymbol(symbol);
            listModel.addElement(rs);
            currentSymbols.add(rs);
        }
        drawComponents();
    }

    List<LudemeNode> defineNodes;

    // for defines
    public void updateList(List<Symbol> symbolList, List<LudemeNode> nodes)
    {
        this.defineNodes = nodes;
        // remove duplicates
        List<Symbol> symbolList2 = symbolList;
        symbolList2 = symbolList.stream().distinct().collect(java.util.stream.Collectors.toList());
        currentSymbols = new ArrayList<>();
        searchField.setText("");
        listModel.clear();
        for(Symbol symbol : symbolList2)
        {
            ReadableSymbol rs = new ReadableSymbol(symbol);
            listModel.addElement(rs);
            currentSymbols.add(rs);
        }
        drawComponents();
    }

    private void drawComponents()
    {
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        scrollableList.setPreferredSize(new Dimension(scrollableList.getPreferredSize().width, 150));
        searchField.setPreferredSize(new Dimension(scrollableList.getPreferredSize().width, searchField.getPreferredSize().height));


        add(searchField); add(scrollableList);

        repaint();


        setPreferredSize(new Dimension(getPreferredSize()));
        setSize(getPreferredSize());
        setVisible(false);
    }


    static boolean isConstantTerminal(Symbol s)
    {
        if(s.rule() == null) return false;
        if(s.rule().rhs().size() == 1 && (s.rule().rhs().get(0).args().isEmpty() || s.rule().rhs().get(0).args().get(0).symbol() == s)) return false;
        for(Clause c : s.rule().rhs())
        {
            if(c.args() == null) continue;
            for(ClauseArg ca : c.args())
            {
                if(!ca.symbol().ludemeType().equals(Symbol.LudemeType.Constant)) return false;
            }
        }
        return true;
    }

}
