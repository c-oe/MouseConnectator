import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * @author Coe
 * @since 2023-05-21 18:30
 */
public class Window extends JFrame {
    public static void main(String[] args) {
        new Window();
    }

    private final static Pattern numPattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
    private final static Pattern timePattern = Pattern.compile("^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|1[0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$");

    private String num = "";

    private String time = "";

    private final String exampleTime =  "2000-10-14 12:00:00";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    JPanel mainPanel = new JPanel();//布局
    JLabel infoLabel1 = new JLabel("不设置时间，每天 00:00:00 执行");//提示1
    JLabel infoLabel2 = new JLabel("点击开始时在设定时间内将鼠标移至点击位置");//提示2
    JLabel infoLabel3 = new JLabel("点击立即开始时在 1 秒内移动鼠标至点击位置");//提示3
    JLabel dateLabel = new JLabel("定时时间：");
    JFormattedTextField dateTextField = new JFormattedTextField(exampleTime);//定时时间输入框
    JLabel numLabel = new JLabel("*连点次数：");
    JFormattedTextField numTextField = new JFormattedTextField("100");//次数输入框
    JButton nowButton = new JButton("当前时间");
    JButton startButton = new JButton("开始");
    JButton startNowButton = new JButton("立即开始");
    JLabel infoLabel4 = new JLabel("",JLabel.CENTER);//提示4

    public Window() {
        //窗口风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //窗口名称
        setTitle("鼠标连点器");
        //设置窗口是否可视
        setVisible(true);
        //设置窗口的大小是否可以调节
        setResizable(false);
        //设置窗口大小和x,y位置
        setSize(500, 400);
        //设置窗口退出则程序退出
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //窗口在屏幕中央显示
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        mainPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(255,255,255));

        infoLabel1.setFont(new Font("PingFang SC", Font.BOLD, 16));
        infoLabel1.setForeground(Color.BLACK);
        infoLabel1.setBounds(120, 10, 1000, 38);
        infoLabel1.setOpaque(false);
        mainPanel.add(infoLabel1);

        infoLabel2.setFont(new Font("PingFang SC", Font.BOLD, 16));
        infoLabel2.setForeground(Color.BLACK);
        infoLabel2.setBounds(80, 40, 1000, 38);
        infoLabel2.setOpaque(false);
        mainPanel.add(infoLabel2);

        infoLabel3.setFont(new Font("PingFang SC", Font.BOLD, 16));
        infoLabel3.setForeground(Color.BLACK);
        infoLabel3.setBounds(80, 70, 1000, 38);
        infoLabel3.setOpaque(false);
        mainPanel.add(infoLabel3);

        dateLabel.setFont(new Font("PingFang SC", Font.BOLD, 16));
        dateLabel.setForeground(Color.ORANGE);
        dateLabel.setBounds(100, 120, 91, 38);
        dateLabel.setOpaque(false);
        mainPanel.add(dateLabel);

        dateTextField.setForeground(Color.LIGHT_GRAY);
        dateTextField.setBounds(200, 125, 150, 33);
        dateTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (exampleTime.equals(dateTextField.getText())){
                    dateTextField.setText("");                          //将提示文字清空
                    dateTextField.setForeground(Color.BLACK);           //设置用户输入的字体颜色为黑色
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                String text = dateTextField.getText();
                if ("".equals(text)){
                    dateTextField.setForeground(Color.LIGHT_GRAY);      //将提示文字设置为灰色
                    dateTextField.setText(exampleTime);                 //显示提示文字
                }else {
                    time = text;
                }
            }
        });
        mainPanel.add(dateTextField);

        numLabel.setFont(new Font("PingFang SC", Font.BOLD, 16));
        numLabel.setForeground(Color.RED);
        numLabel.setBounds(100, 180, 91, 38);
        numLabel.setOpaque(false);
        mainPanel.add(numLabel);

        numTextField.setForeground(Color.LIGHT_GRAY);
        numTextField.setBounds(200, 185, 50, 33);
        numTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if ("100".equals(numTextField.getText())){
                    numTextField.setText("");                          //将提示文字清空
                    numTextField.setForeground(Color.BLACK);           //设置用户输入的字体颜色为黑色
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                String text = numTextField.getText();
                if ("".equals(text)){
                    numTextField.setForeground(Color.LIGHT_GRAY);      //将提示文字设置为灰色
                    numTextField.setText("100");                       //显示提示文字
                }else {
                    num = text;
                }
            }
        });
        mainPanel.add(numTextField);

        nowButton.setBounds(360,130,90,20);
        nowButton.addActionListener(e -> {
            String nowTime = sdf.format(new Date());
            time = nowTime;
            dateTextField.setText(nowTime);
            dateTextField.setForeground(Color.BLACK);
        });
        mainPanel.add(nowButton);

        startButton.setBounds(120,240,75,50);
        startButton.addActionListener(e -> {
            if (numPattern.matcher(num).matches()) {
                int realNum = Integer.parseInt(num);
                if (realNum <= 0){
                    JOptionPane.showMessageDialog(null, "点击次数必填！", "错误", JOptionPane.ERROR_MESSAGE);
                }else {
                    if (!exampleTime.equals(time) && timePattern.matcher(time).matches()) {
                        Date date;
                        try {
                            date = sdf.parse(time);
                        } catch (ParseException ex) {
                            JOptionPane.showMessageDialog(null, "程序执行错误！", "错误", JOptionPane.ERROR_MESSAGE);
                            throw new RuntimeException(ex);
                        }
                        if (date.after(new Date())) {
                            timeTask(time, realNum, false);
                            disabled(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "执行时间不能在当前时间之前！", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if ("".equals(time)) {//不指定时间
                        //获取明天的日期
                        LocalDate date = LocalDate.now().minusDays(-1);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String format = date.format(formatter);

                        //拼接执行时间
                        time = format + " 00:00:00";

                        timeTask(time, realNum, false);
                        disabled(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "时间格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else {
                JOptionPane.showMessageDialog(null, "次数必须为数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(startButton);

        startNowButton.setBounds(240,240,150,50);
        startNowButton.addActionListener(e -> {
            if (numPattern.matcher(num).matches()) {
                int realNum = Integer.parseInt(num);
                if (realNum <= 0){
                    JOptionPane.showMessageDialog(null, "点击次数必填！", "错误", JOptionPane.ERROR_MESSAGE);
                }else {
                    timeTask(time, realNum,true);
                    disabled(true);
                }
            }else {
                JOptionPane.showMessageDialog(null, "次数必须为数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(startNowButton);

        infoLabel4.setFont(new Font("PingFang SC", Font.BOLD, 16));
        infoLabel4.setForeground(Color.BLACK);
        infoLabel4.setBounds(0, 300, 500, 38);
        infoLabel4.setOpaque(false);
        mainPanel.add(infoLabel4);
    }

    /**
     * 按钮与输入框控制
     * @param now 是否立即执行
     */
    public void disabled(boolean now){
        if (now){
            //按钮不可用
            startButton.setEnabled(false);
            startNowButton.setEnabled(false);
            //重置
            numTextField.setForeground(Color.LIGHT_GRAY);
            numTextField.setText("100");
            this.num = "";
        }else {
            //按钮不可用
            startButton.setEnabled(false);
            startNowButton.setEnabled(false);
            //重置
            dateTextField.setForeground(Color.LIGHT_GRAY);
            dateTextField.setText(exampleTime);
            numTextField.setForeground(Color.LIGHT_GRAY);
            numTextField.setText("100");
            this.time = "";
            this.num = "";
        }
    }

    /**
     * 定时点击
     * @param time 时间
     * @param num 次数
     */
    public void timeTask(String time ,int num,boolean now){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    click(num);
                    JOptionPane.showMessageDialog(null, "点击完毕!", "成功", JOptionPane.INFORMATION_MESSAGE);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            startButton.setEnabled(true);
                            startNowButton.setEnabled(true);
                        }
                    },0);
                    infoLabel4.setText("");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "程序执行错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(e);
                }
            }
        };

        //立即执行
        if (now) {
            //创建定时任务
            timer.schedule(task, 1000);//等待1秒执行
            infoLabel4.setText("立即执行" + num + "次");
        } else {
            Date startDateTime;
            try {
                startDateTime = sdf.parse(time);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "程序执行错误！", "错误", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }
            timer.schedule(task, startDateTime);
            infoLabel4.setText("在 " + time + " 执行" + num + "次");
        }
    }

    /**
     * 点击方法
     * @param num 点击数量
     * @throws Exception 异常信息
     */
    public static void click(int num) throws Exception {
        Robot robot = new Robot();
        while (num-- > 0) {
            System.out.println("点击剩余" + num + "次");
            robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(34);//QQ飞车 30 帧为 33.34 毫秒
        }
    }
}