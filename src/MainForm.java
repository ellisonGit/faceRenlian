import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import main.java.com.ha.facecamera.configserver.ConfigServer;
import main.java.com.ha.facecamera.configserver.ConfigServerConfig;
import main.java.com.ha.facecamera.configserver.ConnectStateInvokeCondition;
import main.java.com.ha.facecamera.configserver.DataServer;
import main.java.com.ha.facecamera.configserver.DataServerConfig;
import main.java.com.ha.facecamera.configserver.pojo.AppConfig;
import main.java.com.ha.facecamera.configserver.pojo.Face;
import main.java.com.ha.facecamera.configserver.pojo.FacePage;
import main.java.com.ha.facecamera.configserver.pojo.ListFaceCriteria;
import main.java.com.ha.facecamera.configserver.pojo.NetConfig;
import main.java.com.ha.facecamera.configserver.pojo.Time;
import main.java.com.ha.sdk.HaCamera;

public class MainForm extends JFrame {
	
	private static final long serialVersionUID = -4081599548364704057L;
	
	private JPanel contentPane;
	private JTextField textField;
	private ConfigServer configServer;
	private DataServer dataServer;
	private JTextArea textArea;
	private JTextField textField_1;
	private DataViewDialog dataViewDialog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				MainForm frame = new MainForm();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1366, 766);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		configServer = new ConfigServer();
		dataServer = new DataServer();
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("\u7AEF\u53E3\uFF1A");
		panel_1.add(lblNewLabel);
		
		textField = new JTextField();
		panel_1.add(textField);
		textField.setText("10001");
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("\u76D1\u542C");
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u83B7\u53D6\u5E94\u7528\u53C2\u6570");
		btnNewButton_1.addActionListener((e) -> {
			String sn = JOptionPane.showInputDialog("通过sn获取");
			if(sn == null || sn.isEmpty()) return;
			AppConfig appConfig = configServer.getAppConfig(sn);
			if(appConfig == null) {
				textArea.append("获取sn为");
				textArea.append(sn);
				textArea.append("的应用参数失败！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(configServer.getLastErrorMsg());
				textArea.append("\n");
			} else {
				textArea.append("获取sn为");
				textArea.append(sn);
				textArea.append("的应用参数成功！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(appConfig.toJson());
				textArea.append("\n");
			}
		});
		
		JButton button_2 = new JButton("重启");
		button_2.addActionListener((e) -> {
			String sn = JOptionPane.showInputDialog("通过sn重启");
			if(sn == null || sn.isEmpty()) return;
			boolean ret = configServer.reboot(sn, 500);
			if(!ret) {
				textArea.append("重启sn为");
				textArea.append(sn);
				textArea.append("的设备失败！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(configServer.getLastErrorMsg());
				textArea.append("\n");
			} else {
				textArea.append("重启sn为");
				textArea.append(sn);
				textArea.append("的设备成功！");
				textArea.append("\n");
			}
		});
		panel_1.add(button_2);
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("\u83B7\u53D6\u8BBE\u5907\u65F6\u95F4");
		btnNewButton_2.addActionListener((e) -> {
			String sn = JOptionPane.showInputDialog("通过sn获取");
			if(sn == null || sn.isEmpty()) return;
			Time time = configServer.getTime(sn, 5000);
			if(time == null) {
				textArea.append("获取sn为");
				textArea.append(sn);
				textArea.append("的设备时间失败！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(configServer.getLastErrorMsg());
				textArea.append("\n");
			} else {
				textArea.append("获取sn为");
				textArea.append(sn);
				textArea.append("的设备时间成功！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(time.toJson());
				textArea.append("\n");
			}
		});
		panel_1.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("\u8BBE\u7F6E\u8BBE\u5907\u65F6\u95F4");
		btnNewButton_3.addActionListener((e) -> {
			String sn = JOptionPane.showInputDialog("填入需要设置的设备sn");
			if(sn == null || sn.isEmpty()) return;
			String timeStr = JOptionPane.showInputDialog(MainForm.this, "填入需要设置的时间", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			if(timeStr == null || timeStr.isEmpty()) return;
			Time time = new Time();
			String[] timeStrs = timeStr.split(" ");
			time.setDate(timeStrs[0]);
			time.setTime(timeStrs[1]);
			if(!configServer.setTime(sn, time, 10000)) {
				textArea.append("设置sn为");
				textArea.append(sn);
				textArea.append("的设备时间失败！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(configServer.getLastErrorMsg());
				textArea.append("\n");
			} else {
				textArea.append("设置sn为");
				textArea.append(sn);
				textArea.append("的设备时间成功！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(time.toJson());
				textArea.append("\n");
			}
		});
		panel_1.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("\u67E5\u524D20\u6761\u7279\u5F81\u503C");
		btnNewButton_4.addActionListener((e) -> {
			String sn = JOptionPane.showInputDialog("填入需要设置的设备sn");
			if(sn == null || sn.isEmpty()) return;
			FacePage pfp = configServer.listFace(sn, new ListFaceCriteria(), 15000);
			if(pfp == null) {
				textArea.append("获取sn为");
				textArea.append(sn);
				textArea.append("的前20条特征值失败！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(configServer.getLastErrorMsg());
				textArea.append("\n");
			} else {
				textArea.append("获取sn为");
				textArea.append(sn);
				textArea.append("的前20条特征值成功！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(pfp.toJson());
				textArea.append("\n");
			}
		});
		panel_1.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("\u6DFB\u52A0\u4EBA\u8138");
		btnNewButton_5.addActionListener((e) -> {
			String sn = JOptionPane.showInputDialog("填入需要设置的设备sn");
			if(sn == null || sn.isEmpty()) return;
			AddFaceDialog afd = new AddFaceDialog();
			afd.setVisible(true);				
			Face face = afd.getFaceToUpload();
			if(face == null) return;
			if(!configServer.addFace(sn, face, 5000)) {
				textArea.append("向sn为");
				textArea.append(sn);
				textArea.append("的设备添加人脸失败！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(configServer.getLastErrorMsg());
				textArea.append("\n");
			} else {
				textArea.append("设置sn为");
				textArea.append(sn);
				textArea.append("的设备添加人脸成功！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(face.toJson());
				textArea.append("\n");
			}
		});
		
		JButton button_1 = new JButton("设置参数");
		panel_1.add(button_1);
		button_1.addActionListener((e) -> {
			String sn = JOptionPane.showInputDialog("填入需要设置的设备sn");
			if(sn == null || sn.isEmpty()) return;
			AppConfig appConfig = configServer.getAppConfig(sn);
			if(appConfig == null) {
				textArea.append("获取sn为");
				textArea.append(sn);
				textArea.append("的应用参数失败！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(configServer.getLastErrorMsg());
				textArea.append("\n");
			} else {
				ConfigDialog cd = new ConfigDialog(appConfig);
				cd.setVisible(true);
				boolean ret = configServer.setAppConfig(sn, appConfig, 500);
				if(!ret){
					textArea.append("设置sn为");
					textArea.append(sn);
					textArea.append("的参数失败！");
					textArea.append("\n");
					textArea.append("\t");
					textArea.append(configServer.getLastErrorMsg());
					textArea.append("\n");
				} else {
					textArea.append("设置sn为");
					textArea.append(sn);
					textArea.append("的参数成功！");
					textArea.append("\n");
					textArea.append("\t");
					textArea.append(appConfig.toJson());
					textArea.append("\n");
				}
			}
		});
		// XXX
		panel_1.add(btnNewButton_5);
		
		JButton button_3 = new JButton("获取网络参数");
		panel_1.add(button_3);
		
		button_3.addActionListener((e) -> {
			String sn = JOptionPane.showInputDialog("通过sn获取");
			if(sn == null || sn.isEmpty()) return;
			NetConfig netConfig = configServer.getNetConfig(sn);
			if(netConfig == null) {
				textArea.append("获取sn为");
				textArea.append(sn);
				textArea.append("的网络参数失败！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(configServer.getLastErrorMsg());
				textArea.append("\n");
			} else {
				textArea.append("获取sn为");
				textArea.append(sn);
				textArea.append("的网络参数成功！");
				textArea.append("\n");
				textArea.append("\t");
				textArea.append(netConfig.toJson());
				textArea.append("\n");
			}
		});
		
		JButton button_4 = new JButton("人脸校验√");
		button_4.addActionListener(e -> {
			if(button_4.getText().equals("人脸校验√")) {
				button_4.setText("人脸校验×");
				HaCamera.switchFaceCheckEnable(false);
			} else {
				button_4.setText("人脸校验√");
				HaCamera.switchFaceCheckEnable(true);
			}
		});
		panel_1.add(button_4);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel label = new JLabel("端口：");
		panel_2.add(label);
		
		textField_1 = new JTextField();
		panel_2.add(textField_1);
		textField_1.setText("10002");
		textField_1.setColumns(10);
		
		JButton button = new JButton("监听");
		button.addActionListener((e) -> {
			DataServerConfig dsc = new DataServerConfig();
			dsc.connectStateInvokeCondition = ConnectStateInvokeCondition.DeviceNoKnown;
			boolean ret = dataServer.start(Integer.parseInt(textField_1.getText()), dsc);
			if(ret) {
				textArea.append("启动数据服务器成功！");
				textArea.append("\n");
				dataViewDialog.setVisible(true);
				dataServer.onCameraConnected((val) -> {
					try {
						EventQueue.invokeAndWait(() -> {
							textArea.append("设备已连接-数据端口 ");
							textArea.append(val);
							textArea.append("\n");
						});
					} catch (InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				dataServer.onCameraDisconnected((val) -> {
					try {
						EventQueue.invokeAndWait(() -> {
							textArea.append("设备已断开-数据端口 ");
							textArea.append(val);
							textArea.append("\n");
						});
					} catch (InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				dataServer.onCaptureCompareDataReceived((data) -> {
					try {
						EventQueue.invokeAndWait(() -> {
							textArea.append("收到抓拍对比数据\n\t");
							textArea.append(data.toJson());
							textArea.append("\n");
							dataViewDialog.showCaptureData(data);
							/*try {
								Files.write(new File("C:\\Users\\肖何\\Desktop\\data.json").toPath(), data.toJson().getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
								Files.write(new File("C:\\Users\\肖何\\Desktop\\data.json").toPath(), System.lineSeparator().getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}*/
						});
					} catch (InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
				dataServer.onAuthing((time, username, password) -> {
					// XXX 如果要校验用户名密码，请使用如下代码
					//return username.equals("xiaohe") && Util.judgeRegPassword(time, "123456", password);
					System.out.println(String.format("dataServer 收到鉴权消息username=>%s password=>%s", username, new String(password)));
					return true;
				});
			}
		});
		panel_2.add(button);
		btnNewButton.addActionListener((e) -> {
				ConfigServerConfig csc = new ConfigServerConfig();
				if(JOptionPane.showConfirmDialog(MainForm.this, "是否启用无人脸模式？（启动监听会崩溃的选是）") == JOptionPane.OK_OPTION) {
					csc.noNativeMode = false;
				}
				//csc.connectStateInvokeCondition = main.java.com.ha.facecamera.configserver.ConnectStateInvokeCondition.DeviceNoKnown;
				boolean ret = configServer.start(Integer.parseInt(textField.getText()), csc);
				if(ret) {
					textArea.append("启动配置服务器成功！");
					textArea.append("\n");
					configServer.onCameraConnected((val) -> {
						try {
							EventQueue.invokeAndWait(() -> {
								textArea.append("设备已连接-配置端口 ");
								textArea.append(val);
								textArea.append("\n\tAppConfig:\n\t\t");
								textArea.append(configServer.getAppConfig(val).toJson());
								textArea.append("\n\tVersion:\n\t\t");
								textArea.append(configServer.getVersion(val).toJson());
								textArea.append("\n");
							});
						} catch (InvocationTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
					configServer.onCameraDisconnected((val) -> {
						try {
							EventQueue.invokeAndWait(() -> {
								textArea.append("设备已断开-配置端口 ");
								textArea.append(val);
								textArea.append("\n\tAppConfig:\n\t\t");
								textArea.append(configServer.getAppConfig(val).toJson());
								textArea.append("\n\tVersion:\n\t\t");
								textArea.append(configServer.getVersion(val).toJson());
								textArea.append("\n");
							});
						} catch (InvocationTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
					configServer.onAuthing((time, username, password) -> {
						// XXX 如果要校验用户名密码，请使用如下代码
						//return username.equals("xiaohe") && Util.judgeRegPassword(time, "123456", password);
						System.out.println(String.format("configServer 收到鉴权消息username=>%s password=>%s", username, new String(password)));
						return true;
					});
				}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setDocument(new LimitativeDocument(textArea, 50));
		scrollPane.setViewportView(textArea);

		dataViewDialog = new DataViewDialog(this);
		dataViewDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	}

	
	void addFaceFromCaptureData(String sn1, byte[] featureImageData) {
		String sn = JOptionPane.showInputDialog("填入需要设置的设备sn", sn1);
		if(sn == null || sn.isEmpty()) return;
		AddFaceDialog afd = new AddFaceDialog(featureImageData);
		afd.setVisible(true);
		Face face = afd.getFaceToUpload();
		if(face == null) return;
		if(!configServer.addFace(sn, face, 5000)) {
			textArea.append("向sn为");
			textArea.append(sn);
			textArea.append("的设备添加人脸失败！");
			textArea.append("\n");
			textArea.append("\t");
			textArea.append(configServer.getLastErrorMsg());
			textArea.append("\n");
		} else {
			textArea.append("设置sn为");
			textArea.append(sn);
			textArea.append("的设备添加人脸成功！");
			textArea.append("\n");
			textArea.append("\t");
			textArea.append(face.toJson());
			textArea.append("\n");
		}
	}

	public class LimitativeDocument extends PlainDocument{
		private static final long serialVersionUID = -5038185503502384746L;
		private JTextComponent textComponent;
	    private int lineMax = 10;
	    public   LimitativeDocument(JTextComponent tc,int lineMax){     
	        textComponent = tc;
	        this.lineMax = lineMax;
	    }
	    public   LimitativeDocument(JTextComponent tc){     
	        textComponent = tc;
	    }
	    public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException {
	        
	        String value =   textComponent.getText();
	        long lineCount = IntStream.range(0,value.length()).parallel().filter(c -> value.charAt(c) == '\n').count();
	        if(lineCount >= lineMax) {
	        	int removeStartIdx = 0;
	        	long removeDoneLineCount = lineCount;
	        	while(true) {
	        		if(value.charAt(removeStartIdx++) == '\n' && --removeDoneLineCount < lineMax) {
	        			break;
	        		}
	        	}
	        	offset -= removeStartIdx;
	        	super.remove(0, removeStartIdx);
	        }
	        super.insertString(offset, s, attributeSet);
	    }
	}
}
