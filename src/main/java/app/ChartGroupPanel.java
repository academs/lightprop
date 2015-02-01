package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import math.Function;
import math.Interval;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYDataImageAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultHeatMapDataset;
import org.jfree.data.general.HeatMapDataset;
import org.jfree.data.general.HeatMapUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;

public final class ChartGroupPanel extends JPanel
        implements ChangeListener, ChartChangeListener {

    private HeatMapDataset dataset;
    private JFreeChart mainChart;
    private final JFreeChart subchart1;
    private final JFreeChart subchart2;
    private final JSlider sliderFixT;
    private final JSlider sliderFixX;
    private final Crosshair crosshairXFixed;
    private final Crosshair crosshairTFiexed;
    private final Function<Double> function;
    private final Interval x;
    private final Interval t;
    private final double z;
    private double maxZ = Double.NEGATIVE_INFINITY;
    private double minZ = Double.POSITIVE_INFINITY;
    private Range lastXRange;
    private Range lastYRange;

    public JPanel createMainPanel() {
        mainChart = createChart(new XYSeriesCollection());
        mainChart.addChangeListener(this);
        ChartPanel chartpanel = new ChartPanel(mainChart);
        chartpanel.setFillZoomRectangle(true);
        chartpanel.setMouseWheelEnabled(true);
        return chartpanel;
    }

    @Override
    public void stateChanged(ChangeEvent changeevent) {
        if (changeevent.getSource() == sliderFixT) {
            int i = sliderFixT.getValue() - sliderFixT.getMinimum();
            double ft = (t.getEnd() - t.getStart()) * i / t.getN() + t.getStart();
            crosshairTFiexed.setValue(ft);
            XYDataset xydataset = HeatMapUtilities.extractRowFromHeatMapDataset(dataset, i, "Y1");
            subchart2.getXYPlot().setDataset(xydataset);
        } else if (changeevent.getSource() == sliderFixX) {
            int j = sliderFixX.getValue() - sliderFixX.getMinimum();
            double fx = (x.getEnd() - x.getStart()) * j / x.getN() + x.getStart();
            crosshairXFixed.setValue(fx);
            XYDataset xydataset1 = HeatMapUtilities.extractColumnFromHeatMapDataset(dataset, j, "Y2");
            subchart1.getXYPlot().setDataset(xydataset1);
        }
    }

    @Override
    public void chartChanged(ChartChangeEvent chartchangeevent) {
        XYPlot xyplot = (XYPlot) mainChart.getPlot();
        if (!xyplot.getDomainAxis().getRange().equals(lastXRange)) {
            lastXRange = xyplot.getDomainAxis().getRange();
            XYPlot xyplot1 = (XYPlot) subchart2.getPlot();
            xyplot1.getDomainAxis().setRange(lastXRange);
        }
        if (!xyplot.getRangeAxis().getRange().equals(lastYRange)) {
            lastYRange = xyplot.getRangeAxis().getRange();
            XYPlot xyplot2 = (XYPlot) subchart1.getPlot();
            xyplot2.getDomainAxis().setRange(lastYRange);
        }
    }

    private JFreeChart createChart(XYDataset xydataset) {
        JFreeChart jfreechart = ChartFactory
                .createScatterPlot("F(x,t)", "x", "t", xydataset,
                        PlotOrientation.VERTICAL, true, false, false);
        dataset = createMapDataset();
        GrayPaintScale graypaintscale = new GrayPaintScale(minZ - 0.01, maxZ + 0.01, 128);
        java.awt.image.BufferedImage bufferedimage = HeatMapUtilities
                .createHeatMapImage(dataset, graypaintscale);
        XYDataImageAnnotation xydataimageannotation
                = new XYDataImageAnnotation(bufferedimage, x.getStart(), t.getStart(),
                        x.getEnd() - x.getStart(), t.getEnd() - t.getStart(), true);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setDomainPannable(true);
        xyplot.setRangePannable(true);
        xyplot.getRenderer().addAnnotation(xydataimageannotation, Layer.BACKGROUND);
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setLowerMargin(0.0D);
        numberaxis.setUpperMargin(0.0D);
        NumberAxis numberaxis1 = (NumberAxis) xyplot.getRangeAxis();
        numberaxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis1.setLowerMargin(0.0D);
        numberaxis1.setUpperMargin(0.0D);
        return jfreechart;
    }

    public ChartGroupPanel(Function<Double> function, Interval x, Interval t,
            double z) {
        super(new BorderLayout());
        this.function = function;
        this.x = x;
        this.t = t;
        this.z = z;
        ChartPanel chartpanel = (ChartPanel) createMainPanel();
        chartpanel.setPreferredSize(new Dimension(500, 270));
        CrosshairOverlay crosshairoverlay = new CrosshairOverlay();
        crosshairXFixed = new Crosshair(0.0D);
        crosshairXFixed.setPaint(Color.red);
        crosshairTFiexed = new Crosshair(0.0D);
        crosshairTFiexed.setPaint(Color.blue);
        crosshairoverlay.addDomainCrosshair(crosshairXFixed);
        crosshairoverlay.addRangeCrosshair(crosshairTFiexed);
        chartpanel.addOverlay(crosshairoverlay);
        crosshairXFixed.setLabelVisible(true);
        crosshairXFixed.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
        crosshairXFixed.setLabelBackgroundPaint(new Color(255, 255, 0, 100));
        crosshairTFiexed.setLabelVisible(true);
        crosshairTFiexed.setLabelBackgroundPaint(new Color(255, 255, 0, 100));
        add(chartpanel);
        JPanel jpanel = new JPanel(new BorderLayout());
        XYSeriesCollection xyseriescollection = new XYSeriesCollection();
        subchart1 = ChartFactory.createXYLineChart("F(t, x=const)", "t", "F",
                xyseriescollection, PlotOrientation.HORIZONTAL, false, false, false);
        XYPlot xyplot = (XYPlot) subchart1.getPlot();
        xyplot.getDomainAxis().setLowerMargin(0.0D);
        xyplot.getDomainAxis().setUpperMargin(0.0D);
        xyplot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        ChartPanel chartpanel1 = new ChartPanel(subchart1);
        chartpanel1.setMinimumDrawWidth(0);
        chartpanel1.setMinimumDrawHeight(0);
        chartpanel1.setPreferredSize(new Dimension(200, 150));
        sliderFixT = new JSlider(0, t.getN(), 0);
        sliderFixT.addChangeListener(this);
        sliderFixT.setOrientation(1);
        jpanel.add(chartpanel1);
        jpanel.add(sliderFixT, "West");
        JPanel jpanelX = new JPanel(new BorderLayout());
        XYSeriesCollection xyseriescollection1 = new XYSeriesCollection();
        subchart2 = ChartFactory.createXYLineChart("F(t=const, x)", "x", "F",
                xyseriescollection1, PlotOrientation.VERTICAL, false, false, false);
        XYPlot xyplot1 = (XYPlot) subchart2.getPlot();
        xyplot1.getDomainAxis().setLowerMargin(0.0D);
        xyplot1.getDomainAxis().setUpperMargin(0.0D);
        xyplot1.getRenderer().setSeriesPaint(0, Color.blue);
        ChartPanel chartpanel2 = new ChartPanel(subchart2);
        chartpanel2.setMinimumDrawWidth(0);
        chartpanel2.setMinimumDrawHeight(0);
        chartpanel2.setPreferredSize(new Dimension(200, 150));
        JPanel jpanel2 = new JPanel();
        jpanel2.setPreferredSize(new Dimension(200, 10));
        jpanelX.add(jpanel2, "East");
        sliderFixX = new JSlider(0, x.getN(), 0);
        sliderFixX.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 200));
        sliderFixX.addChangeListener(this);
        jpanelX.add(chartpanel2);
        jpanelX.add(sliderFixX, "North");
        add(jpanel, "East");
        add(jpanelX, "South");
        mainChart.setNotify(true);
    }

    private HeatMapDataset createMapDataset() {
        DefaultHeatMapDataset defaultheatmapdataset
                = new DefaultHeatMapDataset(x.getN() + 1, t.getN() + 1, x.getStart(),
                        x.getEnd(), t.getStart(), t.getEnd());
        double xi = x.getStart();
        double tj = t.getStart();
        double dx = (x.getEnd() - x.getStart()) / x.getN();
        double dt = (t.getEnd() - t.getStart()) / t.getN();
        double z0;
        for (int i = 0; i <= x.getN(); i++) {
            tj = t.getStart();
            for (int j = 0; j <= t.getN(); j++) {                
                //z0 = Math.cos(tj) + xi*xi;
                z0 = function.compute(xi, tj, z);
                maxZ = Math.max(z0, maxZ);
                minZ = Math.min(z0, minZ);
                defaultheatmapdataset.setZValue(i, j, z0);
                tj += dt;
            }
            xi += dx;
        }
        return defaultheatmapdataset;
    }
}
