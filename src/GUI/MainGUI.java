package GUI;

import FuzzySystem.*;
import FuzzySystem.FuzzyRule.FuzzyOperator;
import Main.*;
import org.jetbrains.annotations.NotNull;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MainGUI {
    private JPanel panelMain;
    private JButton runTheSimulationButton;
    private JTable variablesTable;
    private JTable rulesTable;
    private JButton addVariableButton;
    private JButton addRuleButton;
    private JButton showRulesDetailsButton;
    private JButton showVariablesDetailsButton;

    JMenuBar menuBar;
    JMenu menu, fuzzySystemsSubMenu;
    JMenuItem menuItem;

    public MainGUI() {

        // create fuzzy system menu
        menuBar = new JMenuBar();
        menu = new JMenu("Systems");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("Manage and Control Fuzzy Systems");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("New Fuzzy System", KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Creates New Fuzzy System");

        // TODO create new Fuzzy System window with name and description and add the object to Open Fuzzy System submenu
        menuItem.addActionListener(e -> {
            JTextField nameField = new JTextField("New Fuzzy System");
            JTextArea descriptionField = new JTextArea("This is a New Fuzzy System.");

            JPanel fuzzyPanel = new JPanel();
            fuzzyPanel.add(new JLabel("Name: "));
            fuzzyPanel.add(nameField);
            fuzzyPanel.add(new JLabel("Description: "));
            fuzzyPanel.add(descriptionField);

            int result = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Enter the system’s name and a brief description", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    FuzzySystem fuzzySystem = new FuzzySystem(nameField.getText(), descriptionField.getText());
                    loadSystem(fuzzySystem);

                    menuItem = new JMenuItem(nameField.getText());
                    menuItem.setToolTipText(descriptionField.getText());
                    menuItem.getAccessibleContext().setAccessibleDescription(descriptionField.getText());

                    menuItem.addActionListener(e1 -> {
                        loadSystem(fuzzySystem);
                        Main.fuzzySystem = fuzzySystem;

                    });

                    fuzzySystemsSubMenu.add(menuItem);
                } catch (Exception e1) {
                    errorPopup(e1);
                }
            }
        });

        menu.add(menuItem);

        //a submenu
        fuzzySystemsSubMenu = new JMenu("Open Fuzzy System");
        fuzzySystemsSubMenu.setMnemonic(KeyEvent.VK_A);

        menu.add(fuzzySystemsSubMenu);
        menu.addSeparator();


        //init tables
        String[] col = {"ID", "Name", "Type", "Lower", "Upper"};
        DefaultTableModel model = new DefaultTableModel(null, col);
        variablesTable = new JTable(model);


        String[] col2 = {"ID", "Condition", " Operator", "Condition", "Then"};
        DefaultTableModel model2 = new DefaultTableModel(null, col2);
        rulesTable = new JTable(model2);


        // add action listeners
        addActionListeners();
    }

    private void addActionListeners() {

        // variable buttons

        // Enter the variable’s name, type (IN/OUT) and range ([lower, upper])
        addVariableButton.addActionListener(e -> {
            JTextField nameField = new JTextField("New Var");

            // Type
            ButtonGroup radioButtonType = new ButtonGroup();
            JRadioButton inChoice = new JRadioButton("IN");
            JRadioButton outChoice = new JRadioButton("OUT");
            radioButtonType.add(inChoice);
            radioButtonType.add(outChoice);
            inChoice.setSelected(true);

            // range
            JSpinner lower = new JSpinner();
            JSpinner upper = new JSpinner();

            JPanel fuzzyPanel = new JPanel();
            fuzzyPanel.add(new JLabel("Name: "));
            fuzzyPanel.add(nameField);

            fuzzyPanel.add(new JLabel("Lower: "));
            fuzzyPanel.add(lower);
            fuzzyPanel.add(new JLabel("Upper: "));
            fuzzyPanel.add(upper);

            fuzzyPanel.add(new JLabel("Type: "));
            fuzzyPanel.add(inChoice);
            fuzzyPanel.add(outChoice);

            Variable v = null;
            DefaultTableModel variablesTableModel = (DefaultTableModel) variablesTable.getModel();
            int result = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Enter the variable’s name, type (IN/OUT) and range ([lower, upper])", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    lower.commitEdit();
                    upper.commitEdit();

                    if (inChoice.isSelected()) {
                        v = new Variable(nameField.getText(), (Integer) lower.getValue(), (Integer) upper.getValue(), Variable.VariableType.IN);
                        variablesTableModel.addRow(new String[]{String.valueOf(variablesTable.getRowCount()), nameField.getText(), "IN", String.valueOf(lower.getValue()), String.valueOf(upper.getValue())});
                    } else if (outChoice.isSelected()) {
                        v = new Variable(nameField.getText(), (Integer) lower.getValue(), (Integer) upper.getValue(), Variable.VariableType.OUT);
                        variablesTableModel.addRow(new String[]{String.valueOf(variablesTable.getRowCount()), nameField.getText(), "OUT", String.valueOf(lower.getValue()), String.valueOf(upper.getValue())});
                    }
                    Main.fuzzySystem.addVariable(v);

                } catch (Exception e1) {
                    errorPopup(e1);
                }
            }
        });


        // add Rules
        addRuleButton.addActionListener(e -> {
            // IN_variable set operator IN_variable set => OUT_variable set

            Object[] variablesArray = Main.fuzzySystem.getVariablesList().toArray();

            // IF
            JComboBox<Object> INVar1 = new JComboBox<>(variablesArray);
            final JComboBox[] set = new JComboBox[3];
            INVar1.addActionListener(e1 -> set[0] = getFuzzySetOfVar(Main.fuzzySystem.getVariable(INVar1.getSelectedItem().toString())));


            // operator
            FuzzyOperator[] ops = new FuzzyOperator[]{FuzzyOperator.AND, FuzzyOperator.OR, FuzzyOperator.AND_NOT, FuzzyOperator.OR_NOT};

            JComboBox<FuzzyOperator> op = new JComboBox<>(ops);

            // next
            JComboBox<Object> INVar2 = new JComboBox<>(variablesArray);
            INVar2.addActionListener(e1 -> set[1] = getFuzzySetOfVar(Main.fuzzySystem.getVariable(INVar2.getSelectedItem().toString())));


            // then
            JComboBox<Object> OUTVar = new JComboBox<>(variablesArray);
            OUTVar.addActionListener(e1 -> set[2] = getFuzzySetOfVar(Main.fuzzySystem.getVariable(OUTVar.getSelectedItem().toString())));

            JPanel fuzzyPanel = new JPanel();
            fuzzyPanel.add(new JLabel("IF: "));
            fuzzyPanel.add(INVar1);
            fuzzyPanel.add(set[0]);

            fuzzyPanel.add(op);

            fuzzyPanel.add(INVar2);
            fuzzyPanel.add(set[1]);


            fuzzyPanel.add(new JLabel("Then: "));
            fuzzyPanel.add(OUTVar);
            fuzzyPanel.add(set[2]);


            FuzzyRule rule = new FuzzyRule();
            DefaultTableModel rulesTableModel = (DefaultTableModel) rulesTable.getModel();

            int result = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Enter the rules", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    rule.c1 = new Condition(INVar1.getSelectedItem().toString(), set[0].getSelectedItem().toString());
                    rule.operator = ops[op.getSelectedIndex()];
                    rule.c2 = new Condition(INVar2.getSelectedItem().toString(), set[1].getSelectedItem().toString());

                    rule.output_variable = OUTVar.getSelectedItem().toString();
                    rule.output_value = set[2].getSelectedItem().toString();
                    rulesTableModel.addRow(new String[]{String.valueOf(rulesTableModel.getRowCount()), rule.c1.toString(), rule.operator.toString(), rule.c2.toString(), OUTVar.getSelectedItem().toString() + " " + set[2].getSelectedItem().toString()});

                    Main.fuzzySystem.addRule(rule);
                } catch (Exception e1) {
                    errorPopup(e1);
                }
            }
        });

        runTheSimulationButton.addActionListener(e -> {

            ArrayList<JSpinner> values = new ArrayList<>();
            ArrayList<JLabel> labels = new ArrayList<>();
            JPanel fuzzyPanel = new JPanel();
            for (Variable variable : Main.fuzzySystem.getVariablesList()) {
                if (variable.type == Variable.VariableType.IN) {
                    JLabel name = new JLabel(variable.name);
                    JSpinner val = new JSpinner();

                    labels.add(name);
                    values.add(val);

                    fuzzyPanel.add(name);
                    fuzzyPanel.add(val);
                }
            }

            int result = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Enter the crisp values", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    for (int i = 0; i < values.size(); i++) {
                        Main.fuzzySystem.getVariable(labels.get(i).getText()).value = (Integer) values.get(i).getValue();
                    }


                    // TODO Make new window for progress bar
                    Main.fuzzySystem.Fuzzification();
                    System.out.println("Fuzzification => done");
                    Main.fuzzySystem.Inference();
                    System.out.println("Inference => done");
                    Main.fuzzySystem.Defuzzification();
                    System.out.println("Defuzzification => done");
                    for (Variable variable : Main.fuzzySystem.getVariablesList()) {
                        if (variable.type == Variable.VariableType.OUT) {
                            System.out.printf("The predicted %s is %s (%.1f)%n", variable.name, variable.defuzzified_value, variable.value);
                        }
                    }

                } catch (Exception e1) {
                    errorPopup(e1);
                }
            }

        });
    }

    public void errorPopup(Exception e1) {
        JPanel fuzzyPanel = new JPanel();
        fuzzyPanel.add(new JLabel(e1.getMessage()));
        JOptionPane.showMessageDialog(null, fuzzyPanel, "Error", JOptionPane.WARNING_MESSAGE);
    }

    JComboBox getFuzzySetOfVar(@NotNull Variable variable) {
        JComboBox sets = new JComboBox<FuzzyValue>();
        for (FuzzySet f : variable.fuzzySets) {
            for (FuzzyValue v : f.values) {
                sets.addItem(v);
            }
        }
        return sets;
    }


    // TODO LOAD A SYSTEM IN TABLES
    void loadSystem(FuzzySystem f) {
        // init Tables
        // variables
        String[] col = {"ID", "Name", "Type", "Lower", "Upper"};
        DefaultTableModel variablesTableModel = new DefaultTableModel(null, col);
        for (Variable v : f.getVariablesList()) {
            variablesTableModel.addRow(new String[]{String.valueOf(variablesTableModel.getRowCount()), v.name, v.type.toString(), String.valueOf(v.min), String.valueOf(v.max)});
        }
        variablesTable = new JTable(variablesTableModel);


        // rules
        String[] col2 = {"ID", "Condition", " Operator", "Condition", "Then"};
        DefaultTableModel rulesTableModel = new DefaultTableModel(null, col);
        for (FuzzyRule r : f.getRules()) {
            rulesTableModel.addRow(new String[]{String.valueOf(rulesTableModel.getRowCount()), r.c1.toString(), r.operator.toString(), r.c2.toString(), r.output_variable + " " + r.output_value});
        }
        rulesTable = new JTable(variablesTableModel);

        Main.fuzzySystem = f;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fuzzy Logic Toolbox");
        MainGUI mainGUI = new MainGUI();
        frame.setContentPane(mainGUI.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(mainGUI.menuBar);
        frame.pack();
        frame.setVisible(true);
    }
}
