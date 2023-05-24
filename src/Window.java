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

    private int num = 0;

    private String time = "";

    private final String exampleTime =  "2000-10-14 12:00:00";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    JPanel mainPanel = new JPanel();//布局
    JLabel infoLabel1 = new JLabel("不设置时间，每天 00:00:00 执行");//提示1
    JLabel infoLabel2 = new JLabel("点击开始时在设定时间内将鼠标移至点击位置");//提示2
    JLabel infoLabel3 = new JLabel("点击立即开始时在3秒内移动鼠标至点击位置");//提示3
    JLabel dateLabel = new JLabel("定时时间：");
    JFormattedTextField dateTextField = new JFormattedTextField(exampleTime);//定时时间输入框
    JLabel numLabel = new JLabel("*连点次数：");
    JFormattedTextField numTextField = new JFormattedTextField("100");//次数输入框
    JButton startButton = new JButton("开始");
    JButton startNowButton = new JButton("立即开始");

    public Window() {
        //窗体名称
        setTitle("鼠标连点器");
        //设置窗口是否可视
        setVisible(true);
        //设置窗口的大小是否可以调节
        setResizable(false);
        //设置窗口大小和x,y位置
        setSize(500, 350);
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
                    if (!exampleTime.equals(text) && timePattern.matcher(text).matches()) {
                        time = text;
                        System.out.println("时间格式正确");
                    }else {
                        JOptionPane.showMessageDialog(null, "时间格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
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
                if ("".equals(numTextField.getText())){
                    numTextField.setForeground(Color.LIGHT_GRAY);      //将提示文字设置为灰色
                    numTextField.setText("100");                       //显示提示文字
                }
            }
        });
        numTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document document = e.getDocument();
                try {
                    if (numPattern.matcher(document.getText(0,document.getLength())).matches()){
                        num = Integer.parseInt(document.getText(0,document.getLength()));
                    }
                } catch (BadLocationException ex) {
                    JOptionPane.showMessageDialog(null, "程序执行错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {}
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
        mainPanel.add(numTextField);

        startButton.setBounds(120,240,75,50);
        startButton.addActionListener(e -> {
            if (num <= 0){
                JOptionPane.showMessageDialog(null, "点击次数必填！", "错误", JOptionPane.ERROR_MESSAGE);
            }else {
                if (!"".equals(time)){
                    Date date;
                    try {
                        date = sdf.parse(time);
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(null, "程序执行错误！", "错误", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                    if (date.after(new Date())){
                        startButton.setEnabled(false);
                        startNowButton.setEnabled(false);
                        timeTask(time,num,false);
                    }else {
                        JOptionPane.showMessageDialog(null, "执行时间不能在当前时间之前！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        mainPanel.add(startButton);

        startNowButton.setBounds(240,240,150,50);
        startNowButton.addActionListener(e -> {
            if (num <= 0){
                JOptionPane.showMessageDialog(null, "点击次数必填！", "错误", JOptionPane.ERROR_MESSAGE);
            }else {
                startButton.setEnabled(false);
                startNowButton.setEnabled(false);
                timeTask(time,num,true);
            }
        });
        mainPanel.add(startNowButton);
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
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "程序执行错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(e);
                }
            }
        };

        //立即执行
        if (now) {
            //创建定时任务
            timer.schedule(task, 3000);//等待3秒执行
        } else {
            //不指定时间
            if ("".equals(time)) {
                //获取明天的日期
                LocalDate date = LocalDate.now().minusDays(-1);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String format = date.format(formatter);

                //拼接执行时间
                time = format + " 00:00:00";
            }

            Date startDateTime;
            try {
                startDateTime = sdf.parse(time);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "程序执行错误！", "错误", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(e);
            }

            timer.schedule(task, startDateTime);
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
            Thread.sleep(1);
        }
    }
}