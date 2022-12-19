package GUI;

import FuzzySystem.FuzzySet;
import FuzzySystem.FuzzyValue;
import FuzzySystem.Variable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class VariableWindow {
    JPanel panelMain;
    private JTree v_tree;
    private JSplitPane spliter;
    private JPanel r_window;
    Variable data;
    private DefaultMutableTreeNode fuzzySetsNode;
    private DefaultTreeModel dtm;

    public VariableWindow(Variable data) {
        this.data = data;
        initTree();
    }

    private void newFuzzySetWindow() {

        // Set Name
        JTextField fuzzySetName = new JTextField("Set Name");

        // Type
        FuzzyValue.FuzzyType[] type = new FuzzyValue.FuzzyType[]{FuzzyValue.FuzzyType.TRI, FuzzyValue.FuzzyType.TRAP};

        JComboBox<FuzzyValue.FuzzyType> types = new JComboBox<>(type);

        JTextField ranges = new JTextField("40 45 62");

        JPanel fuzzyPanel = new JPanel();
        fuzzyPanel.setLayout(new BoxLayout(fuzzyPanel, BoxLayout.Y_AXIS));
        fuzzyPanel.add(fuzzySetName);

        fuzzyPanel.add(types);

        fuzzyPanel.add(new JLabel("Values (Spaced Out):"));
        fuzzyPanel.add(ranges);

        // enter name
        JTextField mainName = new JTextField();
        int nameMenu = JOptionPane.showConfirmDialog(null, mainName, "Enter The Fuzzy Set Name", JOptionPane.OK_CANCEL_OPTION);

        if (nameMenu == JOptionPane.OK_OPTION) {
            int result = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Enter The Fuzzy Sets Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {

                FuzzySet fuzzySet = new FuzzySet(mainName.getText());
                DefaultMutableTreeNode set = new DefaultMutableTreeNode(mainName.getText());

                data.fuzzySets.add(fuzzySet);

                while (result == JOptionPane.OK_OPTION) {
                    try {
                        FuzzyValue fuzzyValue = new FuzzyValue();

                        fuzzyValue.name = fuzzySetName.getText();
                        fuzzyValue.type = type[types.getSelectedIndex()];

                        for (String range : ranges.getText().split(" ")) {
                            fuzzyValue.range.add(Integer.parseInt(range));
                        }

                        fuzzySet.values.add(fuzzyValue);

                    } catch (Exception e1) {
                        MainGUI.errorPopup(e1);
                    }
                    result = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Continue ?", JOptionPane.YES_NO_OPTION);
                }

                // Update sets node in Tree
                fuzzySetsNode.remove(fuzzySetsNode.getChildCount() - 1);

                fuzzySetsNode.add(set);

                fuzzySetsNode.add(new DefaultMutableTreeNode("NEW SET"));

                dtm.nodeStructureChanged(fuzzySetsNode);
            }
        }
    }

    public static void main(String[] args) {
        Variable v = new Variable("name", 0, 11, Variable.VariableType.IN);

        JFrame variableFrame = new JFrame(v.name);
        VariableWindow variablewindow = new VariableWindow(v);
        variableFrame.setContentPane(variablewindow.panelMain);
        variableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        variableFrame.setPreferredSize(new Dimension(550, 300));
        variableFrame.pack();
        variableFrame.setVisible(true);

    }

    public void initTree() {
        // root
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

        // Details Node
        DefaultMutableTreeNode details = new DefaultMutableTreeNode("Details");

        // FuzzySets Node
        fuzzySetsNode = new DefaultMutableTreeNode("Fuzzy Sets");
        if (data.fuzzyValues != null) {
            data.fuzzySets.forEach((fuzzySet) -> fuzzySetsNode.add(new DefaultMutableTreeNode(fuzzySet.name)));
        }
        fuzzySetsNode.add(new DefaultMutableTreeNode("NEW SET"));

        root.add(details);
        root.add(fuzzySetsNode);
        dtm = new DefaultTreeModel(root);
        v_tree.setModel(dtm);
        spliter.getLeftComponent().setMinimumSize(new Dimension(140, 0));

        v_tree.addTreeSelectionListener(e -> {
            {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) v_tree.getLastSelectedPathComponent();
                if (selectedNode == null) return;

                r_window = new JPanel();
                r_window.setLayout(new BoxLayout(r_window, BoxLayout.Y_AXIS));
                switch (selectedNode.getUserObject().toString()) {
                    case "Root" -> {
                        JLabel rootLabel = new JLabel("This is All Data Required About The Variable");
                        r_window.add(rootLabel);
                    }
                    case "Details" -> {
                        r_window.add(new JLabel(" Name: " + data.name));
                        r_window.add(new JLabel(" Type: " + data.type.toString()));
                        r_window.add(new JLabel(" Lower: " + data.min));
                        r_window.add(new JLabel(" Upper: " + data.max));
                    }
                    case "Fuzzy Sets" -> {
                        JLabel setsLabel = new JLabel("See All Fuzzy Sets Related to The Variable in Details");
                        r_window.add(setsLabel);
                    }
                    case "NEW SET" -> newFuzzySetWindow();
                    default -> setR_window(selectedNode.getParent().getIndex(selectedNode));
                }
                spliter.setRightComponent(new JScrollPane(r_window));
            }
        });
    }

    public void setR_window(int n) {
        if (n < 0) return;
        FuzzySet set = data.fuzzySets.get(n);
        JLabel setsLabel = new JLabel("Fuzzy Set: " + set.name);
        r_window.add(setsLabel);

        for (FuzzyValue val : set.values) {
            JLabel fuzzyValue = new JLabel("Value" + val.name + "       Type: " + val.type.toString());
            r_window.add(fuzzyValue);
            StringBuilder ranges = new StringBuilder();
            for (Integer i : val.range) {
                ranges.append(i).append("   ");
            }
            JLabel rangesLabel = new JLabel(ranges.toString());
            r_window.add(rangesLabel);
        }
    }
}