package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import math.ComplexPlotMode;
import math.ComplexUtils;
import math.Function;
import math.Interval;
import problem.Problem;
import problem.ProblemParams;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.math3.complex.Complex;
import converter.CachedConverterFactory;
import converter.Converter;
import problem.Parameter;

public class App extends JFrame {

    public static void main(String[] args) {
        new App().test();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                App mc = new App();
                mc.init();
                mc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mc.setVisible(true);
            }
        });

    }

    private void test() {

        Problem p = new Problem(new ProblemParams());
        System.out.println("Result: " + p.sovle().compute(0.0, 0.0, 0.0));
    }

    private void computeButtonClicked() {
        try {
            problemParams = grabParamsFromPanel();
            problem = new Problem(problemParams);
            function = problem.sovle();
            refreshPlot();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void init() {
        plotMode = new JComboBox<>(new DefaultComboBoxModel<String>(
                new String[]{
                    ComplexPlotMode.ABS.getName(),
                    ComplexPlotMode.IM.getName(),
                    ComplexPlotMode.RE.getName()}));
        JPanel paramsPanel = generateParamPanel();
        paramsPanel.add(plotMode, "width 55::,wrap");
        paramsPanel.add(computeButton, "al right");
        try {
            fillParamsPanel();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        // получаем панель с содержимым основного фрейма
        Container pane = getContentPane();
        MigLayout mwLayout = new MigLayout("", "", "[top][]");
        // устанавливаем новый менеджер компоновки MigLayout
        pane.setLayout(mwLayout);
        // добавляем панель с задаваемыми параметрами и панель с графиком
        pane.add(paramsPanel);
        chartPanel = new JPanel();
        chartPanel.setBackground(Color.red);
        chartPanel.setLayout(new BorderLayout());
        pane.add(chartPanel, "grow,push,span");

        // навешиваем событие на нажатие кнопки        
        computeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                computeButtonClicked();
            }
        });

        plotMode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPlot();
            }
        });
        this.pack();
    }

    private JPanel generateParamPanel() {
        // формируем вид панели с параметрами
        // с помощью менеджера компоновки MigLayout 
        MigLayout mig = new MigLayout();
        JPanel paramsPanel = new JPanel(mig);
        for (Field f : ProblemParams.class.getDeclaredFields()) {
            Parameter p = f.getAnnotation(Parameter.class);
            if (p != null) {
                JLabel label = new JLabel(p.name(), SwingConstants.RIGHT);
                JTextField textField = new JTextField();
                f.setAccessible(true);
                paramsPanel.add(label, "al right");
                paramsPanel.add(textField, "width 55:100:,wrap");
                mapping.put(f, textField);
            }
        }
        return paramsPanel;
    }

    private void refreshPlot() {
        ComplexPlotMode mode = ComplexPlotMode.ABS;
        if (plotMode.getSelectedItem().equals(ComplexPlotMode.ABS.getName())) {
            mode = ComplexPlotMode.ABS;
        }
        if (plotMode.getSelectedItem().equals(ComplexPlotMode.IM.getName())) {
            mode = ComplexPlotMode.IM;
        }
        if (plotMode.getSelectedItem().equals(ComplexPlotMode.RE.getName())) {
            mode = ComplexPlotMode.RE;
        }
        Interval x = new Interval(problemParams.getX0(), problemParams.getX1(),
                problemParams.getNx());
        Interval t = new Interval(problemParams.getT0(), problemParams.getT1(),
                problemParams.getNt());
        chartPanel.removeAll();
        chartPanel.add(new ChartGroupPanel(ComplexUtils.useMode(function, mode),
                x, t, problemParams.getZ()));
        System.out.println("Completed");
        chartPanel.updateUI();
    }

    private ProblemParams grabParamsFromPanel() throws InstantiationException, IllegalAccessException {
        Map<Field, Object> enteredValues = new HashMap<>();
        for (Map.Entry<Field, JTextField> entrySet : mapping.entrySet()) {
            Field field = entrySet.getKey();
            JTextField textField = entrySet.getValue();
            Parameter a = field.getAnnotation(Parameter.class);
            Converter converter = CachedConverterFactory.newConveter(a.converter());
            try {
                Object val = converter.asObject(textField.getText());
                enteredValues.put(field, val);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, a.name() + ".\n" + ex.getMessage(),
                        "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return problemParams;
            }
        }
        problemParams = new ProblemParams();
        for (Map.Entry<Field, Object> val : enteredValues.entrySet()) {
            Field field = val.getKey();
            Object value = val.getValue();
            field.set(problemParams, value);
        }
        return problemParams;
    }

    private void fillParamsPanel() throws InstantiationException, IllegalArgumentException, IllegalAccessException {
        for (Map.Entry<Field, JTextField> entrySet : mapping.entrySet()) {
            Field key = entrySet.getKey();
            JTextField value = entrySet.getValue();
            Parameter a = key.getAnnotation(Parameter.class);
            try {
                Converter converter = CachedConverterFactory.newConveter(a.converter());
                value.setText(converter.asString(key.get(problemParams)));
            } catch (Exception ex) {
                throw new IllegalArgumentException(a.name() + ". " + ex.getMessage(), ex);
            }
        }
    }

    private final Map<Field, JTextField> mapping = new HashMap<>();
    private Problem problem;
    private ProblemParams problemParams = new ProblemParams();
    /**
     * Кнопка для построения графика
     */
    private final JButton computeButton = new JButton("Посчитать");
    /**
     * Панель, на которой будет отображаться график
     */
    private JPanel chartPanel;

    private JComboBox<String> plotMode;

    private Function<Complex> function;
}
