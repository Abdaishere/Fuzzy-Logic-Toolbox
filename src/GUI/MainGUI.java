package GUI;

import FuzzySystem.*;
import FuzzySystem.FuzzyRule.FuzzyOperator;
import Main.Main;
import com.formdev.flatlaf.FlatDarculaLaf;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainGUI {
    private JPanel panelMain;
    private JButton runTheSimulationButton;
    private JButton addVariableButton;
    private JButton addRuleButton;
    private JButton showVariablesDetailsButton;
    private JTable variablesTable;
    private JTable rulesTable;
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

        menuItem.addActionListener(e -> {
            JTextField nameField = new JTextField("New Fuzzy System");
            JTextArea descriptionField = new JTextArea("This is a New Fuzzy System.");
            descriptionField.setPreferredSize(new Dimension(300, 190));
            JPanel fuzzyPanel = new JPanel();
            fuzzyPanel.setLayout(new BoxLayout(fuzzyPanel, BoxLayout.Y_AXIS));

            JPanel name = new JPanel();
            JLabel label = new JLabel("Name:");
            name.add(label);
            name.add(nameField);
            nameField.setPreferredSize(new Dimension(300, 25));
            fuzzyPanel.add(name);

            fuzzyPanel.add(descriptionField);

            int result = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Enter the system’s name and a brief description", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    FuzzySystem fuzzySystem = new FuzzySystem(nameField.getText(), descriptionField.getText());
                    loadSystem(fuzzySystem);

                    menuItem = new JMenuItem(nameField.getText());
                    menuItem.setToolTipText(descriptionField.getText());
                    menuItem.getAccessibleContext().setAccessibleDescription(descriptionField.getText());

                    menuItem.addActionListener(e1 -> loadSystem(fuzzySystem));

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

        // initTree table
        String[] col = {"Please Select or Create a New Fuzzy System"};
        DefaultTableModel variablesTableModel = new DefaultTableModel(col, 0);
        variablesTable.setModel(variablesTableModel);

        String[] col2 = {"Please Select or Create a New Fuzzy System"};
        DefaultTableModel rulesTableModel = new DefaultTableModel(col2, 0);
        rulesTable.setModel(rulesTableModel);

        // add action listeners
        addActionListeners();
    }

    private void addActionListeners() {


        // add variable
        addVariableButton.addActionListener(e -> {
            // Enter the variable’s name, type (IN/OUT) and range ([lower, upper])
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

            JLabel label;
            JPanel fuzzyPanel = new JPanel();
            fuzzyPanel.setLayout(new BoxLayout(fuzzyPanel, BoxLayout.Y_AXIS));

            JPanel name = new JPanel();
            label = new JLabel("Name:");
            name.add(label);
            name.add(nameField);
            nameField.setPreferredSize(new Dimension(178, 25));
            fuzzyPanel.add(name);

            JPanel range = new JPanel();
            label = new JLabel("Lower:");
            range.add(label);

            range.add(lower);
            label = new JLabel(" Upper:");
            range.add(label);
            range.add(upper);
            fuzzyPanel.add(range);

            JPanel options = new JPanel();
            label = new JLabel("Type: ");
            options.add(label);
            options.add(inChoice);
            options.add(outChoice);

            fuzzyPanel.add(options);
            Variable v = null;
            DefaultTableModel variablesTableModel = (DefaultTableModel) variablesTable.getModel();
            int result = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Enter the variable’s name, type (IN/OUT) and range ([lower, upper])", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    lower.commitEdit();
                    upper.commitEdit();

                    if (inChoice.isSelected()) {
                        v = new Variable(nameField.getText(), (Integer) lower.getValue(), (Integer) upper.getValue(), Variable.VariableType.IN);
                        variablesTableModel.addRow(new Object[]{String.valueOf(variablesTable.getRowCount()), nameField.getText(), "IN", String.valueOf(lower.getValue()), String.valueOf(upper.getValue())});
                    } else if (outChoice.isSelected()) {
                        v = new Variable(nameField.getText(), (Integer) lower.getValue(), (Integer) upper.getValue(), Variable.VariableType.OUT);
                        variablesTableModel.addRow(new Object[]{String.valueOf(variablesTable.getRowCount()), nameField.getText(), "OUT", String.valueOf(lower.getValue()), String.valueOf(upper.getValue())});
                    }
                    Main.fuzzySystem.addVariable(v);
                    loadSystem(Main.fuzzySystem);
                } catch (Exception e1) {
                    errorPopup(e1);
                }
            }
        });


        // add Rules
        addRuleButton.addActionListener(e -> {
            // IN_variable set operator IN_variable set => OUT_variable set

            // add empty at first
            ArrayList<Object> arrayList = new ArrayList<>(Collections.singleton("         "));
            arrayList.addAll(Main.fuzzySystem.getVariablesList());
            Object[] variablesArray = arrayList.toArray();

            JComboBox[] set = new JComboBox[3];

            // IF
            JComboBox<Object> INVar1 = new JComboBox<>(variablesArray);
            set[0] = new JComboBox<>(new String[]{"Chose Set"});


            // operator
            FuzzyOperator[] ops = new FuzzyOperator[]{FuzzyOperator.AND, FuzzyOperator.OR, FuzzyOperator.AND_NOT, FuzzyOperator.OR_NOT};

            JComboBox<FuzzyOperator> op = new JComboBox<>(ops);

            // next
            JComboBox<Object> INVar2 = new JComboBox<>(variablesArray);
            set[1] = new JComboBox<>(new String[]{"Chose Set"});


            // then
            JComboBox<Object> OUTVar = new JComboBox<>(variablesArray);
            set[2] = new JComboBox<>(new String[]{"Chose Set"});

            JPanel fuzzyPanel = new JPanel();
            fuzzyPanel.setLayout(new BoxLayout(fuzzyPanel, BoxLayout.Y_AXIS));
            try {
                INVar1.addItemListener(e1 -> set[0].setModel(getFuzzySetOfVar(Main.fuzzySystem.getVariable(Objects.requireNonNull(INVar1.getSelectedItem()).toString()))));
                INVar2.addItemListener(e1 -> set[1].setModel(getFuzzySetOfVar(Main.fuzzySystem.getVariable(Objects.requireNonNull(INVar2.getSelectedItem()).toString()))));
                OUTVar.addItemListener(e1 -> set[2].setModel(getFuzzySetOfVar(Main.fuzzySystem.getVariable(Objects.requireNonNull(OUTVar.getSelectedItem()).toString()))));
            } catch (Exception ignored) {
            }

            JLabel label;

            JPanel first = new JPanel();
            label = new JLabel("IF:");
            fuzzyPanel.add(label);
            first.add(INVar1);
            first.add(set[0]);
            fuzzyPanel.add(first);

            op.setMaximumSize(new Dimension(90, 25));
            fuzzyPanel.add(op);

            JPanel second = new JPanel();
            second.add(INVar2);
            second.add(set[1]);
            fuzzyPanel.add(second);

            JPanel then = new JPanel();

            label = new JLabel("Then:");
            fuzzyPanel.add(label);

            then.add(OUTVar);
            then.add(set[2]);
            fuzzyPanel.add(then);


            FuzzyRule rule = new FuzzyRule();
            DefaultTableModel rulesTableModel = (DefaultTableModel) rulesTable.getModel();
            int result = -1;
            try {
                result = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Enter The Rule's Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            } catch (Exception e1) {
                errorPopup(e1);
            }
            if (result == JOptionPane.OK_OPTION) {
                try {
                    rule.c1 = new Condition(Objects.requireNonNull(INVar1.getSelectedItem()).toString(), Objects.requireNonNull(set[0].getSelectedItem()).toString());
                    rule.operator = ops[op.getSelectedIndex()];
                    rule.c2 = new Condition(Objects.requireNonNull(INVar2.getSelectedItem()).toString(), Objects.requireNonNull(set[1].getSelectedItem()).toString());

                    rule.output_variable = Objects.requireNonNull(OUTVar.getSelectedItem()).toString();
                    rule.output_value = Objects.requireNonNull(set[2].getSelectedItem()).toString();
                    rulesTableModel.addRow(new Object[]{String.valueOf(rulesTableModel.getRowCount()), rule.c1.toString(), rule.operator.toString(), rule.c2.toString(), OUTVar.getSelectedItem().toString() + " " + set[2].getSelectedItem().toString()});

                    Main.fuzzySystem.addRule(rule);
                    loadSystem(Main.fuzzySystem);

                } catch (Exception e1) {
                    errorPopup(e1);
                }
            }
        });

        runTheSimulationButton.addActionListener(e -> {

//            if (Main.fuzzySystem.getVariablesList().isEmpty())
//                errorPopup(new Exception("CAN’T START THE SIMULATION! Please add the fuzzy sets and rules first."));

            ArrayList<JSpinner> values = new ArrayList<>();
            ArrayList<JLabel> labels = new ArrayList<>();
            JPanel fuzzyPanel = new JPanel();
            fuzzyPanel.setLayout(new BoxLayout(fuzzyPanel, BoxLayout.Y_AXIS));
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

            int input = JOptionPane.showConfirmDialog(null, fuzzyPanel, "Enter the crisp values", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (input == JOptionPane.OK_OPTION) {
                try {
                    for (int i = 0; i < values.size(); i++) {
                        Main.fuzzySystem.getVariable(labels.get(i).getText()).value = (Integer) values.get(i).getValue();
                    }

                    Main.fuzzySystem.Fuzzification();
                    System.out.println("Fuzzification => done");
                    Main.fuzzySystem.Inference();
                    System.out.println("Inference => done");
                    Main.fuzzySystem.Defuzzification();
                    System.out.println("Defuzzification => done");

                    Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(34, 166, 62), new Color(34, 166, 62));
                    JPanel OUTs = new JPanel();
                    OUTs.setBorder(border);
                    border = BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(33, 143, 156), new Color(33, 143, 156));
                    JPanel IN = new JPanel();
                    IN.setBorder(border);

                    JSplitPane resultWindow = new JSplitPane();

                    for (Variable variable : Main.fuzzySystem.getVariablesList()) {
                        if (variable.type == Variable.VariableType.IN) {
                            IN.add(new JLabel(String.format("%s : %.1f", variable.name, variable.value)));
                        } else if (variable.type == Variable.VariableType.OUT) {
                            System.out.printf("The predicted %s is %s (%.1f)%n", variable.name, variable.defuzzified_value, variable.value);
                            OUTs.add(new JLabel(String.format("%s : %s (%.1f)", variable.name, variable.defuzzified_value, variable.value)));
                        }
                    }

                    resultWindow.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    resultWindow.setTopComponent(IN);
                    resultWindow.setDividerLocation(100);
                    resultWindow.setBottomComponent(OUTs);
                    resultWindow.setPreferredSize(new Dimension(400, 250));

                    JOptionPane.showConfirmDialog(null, resultWindow, "Results", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                } catch (Exception e1) {
                    errorPopup(e1);
                }
            }

        });


        showVariablesDetailsButton.addActionListener(e -> {
            int[] rows = variablesTable.getSelectedRows();
            ArrayList<Variable> variables = Main.fuzzySystem.getVariablesList();
            for (int i : rows) {
                createVariableWindow(variables.get(i));
            }
        });
    }

    void createVariableWindow(Variable v) {
        JFrame variableFrame = new JFrame(v.name);
        VariableWindow variablewindow = new VariableWindow(v);
        variableFrame.setContentPane(variablewindow.panelMain);
        variableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        variableFrame.setPreferredSize(new Dimension(500, 350));
        variableFrame.pack();
        variableFrame.setVisible(true);
    }

    public static void errorPopup(Exception e1) {
        JPanel fuzzyPanel = new JPanel();
        fuzzyPanel.add(new JLabel(e1.getMessage()));
        JOptionPane.showMessageDialog(null, fuzzyPanel, e1.getLocalizedMessage(), JOptionPane.WARNING_MESSAGE);
    }

    private ComboBoxModel getFuzzySetOfVar(@NotNull Variable variable) {
        JComboBox<String> sets = new JComboBox<>();
        for (FuzzySet f : variable.fuzzySets) {
            for (FuzzyValue v : f.values) {
                sets.addItem(v.name);
            }
        }

        if (sets.getItemCount() == 0) {
            sets = new JComboBox<>(new String[]{"Empty"});
        }

        return sets.getModel();
    }

    void loadSystem(FuzzySystem f) {
        // initTree Tables
        // variables
        String[] col = {"ID", "Name", "Type", "Lower", "Upper"};
        DefaultTableModel variablesTableModel = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        for (Variable v : f.getVariablesList()) {
            variablesTableModel.addRow(new Object[]{String.valueOf(variablesTableModel.getRowCount()), v.name, v.type.toString(), String.valueOf(v.min), String.valueOf(v.max)});
        }

        variablesTable.setModel(variablesTableModel);


        // rules
        String[] col2 = {"ID", "Condition", " Operator", "Condition", "Then"};
        DefaultTableModel rulesTableModel = new DefaultTableModel(col2, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        for (FuzzyRule r : f.getRules()) {
            rulesTableModel.addRow(new Object[]{String.valueOf(rulesTableModel.getRowCount()), r.c1.toString(), r.operator.toString(), r.c2.toString(), r.output_variable + " " + r.output_value});
        }
        rulesTable.setModel(rulesTableModel);

        Main.fuzzySystem = f;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        JFrame frame = new JFrame("Fuzzy Logic Toolbox");
        MainGUI mainGUI = new MainGUI();
        frame.setContentPane(mainGUI.panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(mainGUI.menuBar);
        frame.pack();
        frame.setVisible(true);
    }
}
