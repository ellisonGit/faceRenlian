import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import main.java.com.ha.facecamera.configserver.pojo.AppConfig;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;

public class ConfigDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 6892060509298969921L;
	
	private AppConfig appConfig;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtEnsureThreshold;
	private JCheckBox chkCompareSwitch;
	private JCheckBox chkRepeatFilter;
	private JTextField txtRepeatFilterTime;
	private JTextField txtDeviceNo;
	private JTextField txtAddrNo;
	private JTextField txtAddrName;
	private JTextField txtHeartBeatInterval;
	private JComboBox<String> combDataUploadMethod;
	private JTextField txtDataUploadServer;
	private JTextField txtDataUploadPort;
	private JTextField txtDataUploadUrl;
	private JTextField txtDataUploadUsername;
	private JTextField txtDataUploadPassword;
	private JTextField txtCloundConfigIp;
	private JCheckBox chkCloundConfigEnable;
	private JTextField txtCloundConfigPort;
	private JCheckBox chkDetectAge;
	private JCheckBox chkDetectSex;
	private JCheckBox chkDetectLiving;
	private JCheckBox chkSafetyHat;
	private JCheckBox chkUploadEnvironmentImage;
	private JCheckBox chkUploadFeatureImage;
	private JCheckBox chkUploadPhoto;
	private JCheckBox chkUploadFeature;
	private JCheckBox chkDoReg;
	private JTextField txtRegUserName;
	private JTextField txtRegPassword;
	private JComboBox<String> combWorkmode;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ConfigDialog dialog = new ConfigDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConfigDialog(AppConfig _appConfig) {
		this.appConfig = _appConfig;
		setBounds(100, 100, 980, 840);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(0, 3));
			panel.setBorder(BorderFactory.createTitledBorder("设备信息"));			
			{
				JLabel label = new JLabel("设备编号：");
				panel.add(label);
			}
			{
				txtDeviceNo = new JTextField();
				if(this.appConfig != null)
					txtDeviceNo.setText(this.appConfig.getDeviceNo());
				panel.add(txtDeviceNo);
				txtDeviceNo.setColumns(10);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("点位编号：");
				panel.add(label);
			}
			{
				txtAddrNo = new JTextField();
				if(this.appConfig != null)
					txtAddrNo.setText(this.appConfig.getAddrNo());
				panel.add(txtAddrNo);
				txtAddrNo.setColumns(10);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("点位名称：");
				panel.add(label);
			}
			{
				txtAddrName = new JTextField();
				if(this.appConfig != null)
					txtAddrName.setText(this.appConfig.getAddrName());
				panel.add(txtAddrName);
				txtAddrName.setColumns(10);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("心跳间隔：");
				panel.add(label);
			}
			{
				txtHeartBeatInterval = new JTextField();
				if(this.appConfig != null)
					txtHeartBeatInterval.setText(String.valueOf(this.appConfig.getHeartBeatInterval()));
				panel.add(txtHeartBeatInterval);
				txtHeartBeatInterval.setColumns(10);
			}
			{
				JLabel label = new JLabel("（5~255，单位秒）");
				panel.add(label);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setBorder(BorderFactory.createTitledBorder("参数配置"));
			panel.setLayout(new GridLayout(0, 3));
			{
				JLabel label = new JLabel("确信分数：");
				panel.add(label);
			}
			{
				txtEnsureThreshold = new JTextField();
				if(this.appConfig != null)
					txtEnsureThreshold.setText(String.valueOf(this.appConfig.getEnsureThreshold()));
				panel.add(txtEnsureThreshold);
			}
			{
				JLabel label = new JLabel("（70~100）");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("对比开关：");
				panel.add(label);
			}
			{
				chkCompareSwitch = new JCheckBox("");
				if(this.appConfig != null)
					chkCompareSwitch.setSelected(this.appConfig.isCompareSwitch());
				panel.add(chkCompareSwitch);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("去重复开关：");
				panel.add(label);
			}
			{
				chkRepeatFilter = new JCheckBox("");
				if(this.appConfig != null)
					chkRepeatFilter.setSelected(this.appConfig.isRepeatFilter());
				panel.add(chkRepeatFilter);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("去重复超时：");
				panel.add(label);
			}
			{
				txtRepeatFilterTime = new JTextField();
				if(this.appConfig != null)
					txtRepeatFilterTime.setText(String.valueOf(this.appConfig.getRepeatFilterTime()));
				panel.add(txtRepeatFilterTime);
				txtRepeatFilterTime.setColumns(10);
			}
			{
				JLabel label = new JLabel("（3~60，单位秒，去重复关闭时表示输出间隔）");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("检测年龄：");
				panel.add(label);
			}
			{
				chkDetectAge = new JCheckBox("");
				if(this.appConfig != null)
					chkDetectAge.setSelected(this.appConfig.isAgeDetect());
				panel.add(chkDetectAge);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("检测性别：");
				panel.add(label);
			}
			{
				chkDetectSex = new JCheckBox("");
				if(this.appConfig != null)
					chkDetectSex.setSelected(this.appConfig.isSexDetect());
				panel.add(chkDetectSex);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("活体检测：");
				panel.add(label);
			}
			{
				chkDetectLiving = new JCheckBox("");
				if(this.appConfig != null)
					chkDetectLiving.setSelected(this.appConfig.isLivingDetect());
				panel.add(chkDetectLiving);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("安全帽：");
				panel.add(label);
			}
			{
				chkSafetyHat = new JCheckBox("");
				if(this.appConfig != null)
					chkSafetyHat.setSelected(this.appConfig.isSafetyHatDetect());
				panel.add(chkSafetyHat);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("工作模式：");
				panel.add(label);
			}
			{
				combWorkmode = new JComboBox<String>();
				combWorkmode.addItem("自动模式");
				combWorkmode.addItem("在线模式");
				combWorkmode.addItem("离线模式");
				if(this.appConfig != null)
					combWorkmode.setSelectedIndex(this.appConfig.getWorkMode() - 1);
				panel.add(combWorkmode);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(0, 3));
			panel.setBorder(BorderFactory.createTitledBorder("数据上传"));
			{
				JLabel label = new JLabel("上传方式：");
				panel.add(label);
			}
			{
				combDataUploadMethod = new JComboBox<String>();
				combDataUploadMethod.addItem("关闭");
				combDataUploadMethod.addItem("TCP");
				combDataUploadMethod.addItem("FTP");
				combDataUploadMethod.addItem("HTTP");
				if(this.appConfig != null)
					combDataUploadMethod.setSelectedIndex(this.appConfig.getDataUploadMethod());
				panel.add(combDataUploadMethod);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel lblIp = new JLabel("IP：");
				panel.add(lblIp);
			}
			{
				txtDataUploadServer = new JTextField();
				if(this.appConfig != null)
					txtDataUploadServer.setText(this.appConfig.getDataUploadServer());
				panel.add(txtDataUploadServer);
				txtDataUploadServer.setColumns(10);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("端口号：");
				panel.add(label);
			}
			{
				txtDataUploadPort = new JTextField();
				if(this.appConfig != null)
					txtDataUploadPort.setText(String.valueOf(this.appConfig.getDataUploadPort()));
				panel.add(txtDataUploadPort);
				txtDataUploadPort.setColumns(10);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("上传路径：");
				panel.add(label);
			}
			{
				txtDataUploadUrl = new JTextField();
				if(this.appConfig != null)
					txtDataUploadUrl.setText(this.appConfig.getDataUploadUrl());
				panel.add(txtDataUploadUrl);
				txtDataUploadUrl.setColumns(10);
			}
			{
				JLabel lbltcp = new JLabel("（上传路径，TCP设置此值无效）");
				panel.add(lbltcp);
			}
			{
				JLabel label = new JLabel("用户名：");
				panel.add(label);
			}
			{
				txtDataUploadUsername = new JTextField();
				if(this.appConfig != null)
					txtDataUploadUsername.setText(this.appConfig.getFtpPassword());
				panel.add(txtDataUploadUsername);
				txtDataUploadUsername.setColumns(10);
			}
			{
				JLabel lblftp = new JLabel("（只在FTP时有效）");
				panel.add(lblftp);
			}
			{
				JLabel label = new JLabel("密码：");
				panel.add(label);
			}
			{
				txtDataUploadPassword = new JTextField();
				if(this.appConfig != null)
					txtDataUploadPassword.setText(this.appConfig.getFtpPassword());
				panel.add(txtDataUploadPassword);
				txtDataUploadPassword.setColumns(10);
			}
			{
				JLabel label = new JLabel("（只在FTP时有效）");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("上传全景图：");
				panel.add(label);
			}
			{
				chkUploadEnvironmentImage = new JCheckBox("");
				if(this.appConfig != null)
					chkUploadEnvironmentImage.setSelected(this.appConfig.isUploadEnvironmentImage());
				panel.add(chkUploadEnvironmentImage);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("上传特写图：");
				panel.add(label);
			}
			{
				chkUploadFeatureImage = new JCheckBox("");
				if(this.appConfig != null)
					chkUploadFeatureImage.setSelected(this.appConfig.isUploadFeatureImage());
				panel.add(chkUploadFeatureImage);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("上传模板图：");
				panel.add(label);
			}
			{
				chkUploadPhoto = new JCheckBox("");
				if(this.appConfig != null)
					chkUploadPhoto.setSelected(this.appConfig.isUploadPhoto());
				panel.add(chkUploadPhoto);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("上传特征值：");
				panel.add(label);
			}
			{
				chkUploadFeature = new JCheckBox("");
				if(this.appConfig != null)
					chkUploadFeature.setSelected(this.appConfig.isUploadFeature());
				panel.add(chkUploadFeature);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(0, 3));
			panel.setBorder(BorderFactory.createTitledBorder("外网配置"));
			{
				JLabel label = new JLabel("是否启用：");
				panel.add(label);
			}
			{
				chkCloundConfigEnable = new JCheckBox("");
				if(this.appConfig != null)
					chkCloundConfigEnable.setSelected(this.appConfig.isCloundConfigEnable());
				panel.add(chkCloundConfigEnable);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel lblip = new JLabel("服务器IP：");
				panel.add(lblip);
			}
			{
				txtCloundConfigIp = new JTextField();
				if(this.appConfig != null)
					txtCloundConfigIp.setText(this.appConfig.getCloundConfigIp());
				panel.add(txtCloundConfigIp);
				txtCloundConfigIp.setColumns(10);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("服务器端口：");
				panel.add(label);
			}
			{
				txtCloundConfigPort = new JTextField();
				if(this.appConfig != null)
					txtCloundConfigPort.setText(String.valueOf(this.appConfig.getCloundConfigPort()));
				panel.add(txtCloundConfigPort);
				txtCloundConfigPort.setColumns(10);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("开启验证：");
				panel.add(label);
			}
			{
				chkDoReg = new JCheckBox("");
				if(this.appConfig != null)
					chkDoReg.setSelected(this.appConfig.isDoReg());
				panel.add(chkDoReg);
			}
			{
				JLabel label = new JLabel("");
				panel.add(label);
			}
			{
				JLabel label = new JLabel("登陆用户名：");
				panel.add(label);
			}
			{
				txtRegUserName = new JTextField();
				if(this.appConfig != null)
					txtRegUserName.setText(this.appConfig.getRegUserName());
				panel.add(txtRegUserName);
				txtRegUserName.setColumns(10);
			}
			{
				JLabel lblNewLabel = new JLabel("");
				panel.add(lblNewLabel);
			}
			{
				JLabel label = new JLabel("登陆密码：");
				panel.add(label);
			}
			{
				txtRegPassword = new JTextField();
				if(this.appConfig != null)
					txtRegPassword.setText(this.appConfig.getRegPassword());
				panel.add(txtRegPassword);
				txtRegPassword.setColumns(10);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("确定");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("取消");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.appConfig != null) {
			this.appConfig.setDeviceNo(txtDeviceNo.getText());
			this.appConfig.setAddrNo(txtAddrNo.getText());
			this.appConfig.setAddrName(txtAddrName.getText());
			this.appConfig.setHeartBeatInterval(Short.parseShort(txtHeartBeatInterval.getText()));
			this.appConfig.setEnsureThreshold(Integer.parseInt(txtEnsureThreshold.getText()));
			this.appConfig.setUploadEnvironmentImage(chkUploadEnvironmentImage.isSelected());
			this.appConfig.setUploadFeatureImage(chkUploadFeatureImage.isSelected());
			this.appConfig.setCompareSwitch(chkCompareSwitch.isSelected());
			this.appConfig.setRepeatFilter(chkRepeatFilter.isSelected());
			this.appConfig.setRepeatFilterTime(Integer.parseInt(txtRepeatFilterTime.getText()));
			this.appConfig.setDataUploadMethod((short) combDataUploadMethod.getSelectedIndex());
			this.appConfig.setDataUploadServer(txtDataUploadServer.getText());
			this.appConfig.setDataUploadPort(Short.parseShort(txtDataUploadPort.getText()));
			this.appConfig.setDataUploadUrl(txtDataUploadUrl.getText());
			this.appConfig.setFtpUserName(txtDataUploadUsername.getText());
			this.appConfig.setFtpPassword(txtDataUploadPassword.getText());
			this.appConfig.setCloundConfigEnable(chkCloundConfigEnable.isSelected());
			this.appConfig.setCloundConfigIp(txtCloundConfigIp.getText());
			this.appConfig.setCloundConfigPort(Short.parseShort(txtCloundConfigPort.getText()));
			this.appConfig.setAgeDetect(chkDetectAge.isSelected());
			this.appConfig.setSexDetect(chkDetectSex.isSelected());
			this.appConfig.setLivingDetect(chkDetectLiving.isSelected());
			this.appConfig.setSafetyHatDetect(chkSafetyHat.isSelected());
			this.appConfig.setUploadPhoto(chkUploadPhoto.isSelected());
			this.appConfig.setUploadFeature(chkUploadFeature.isSelected());
			this.appConfig.setDoReg(chkDoReg.isSelected());
			this.appConfig.setRegUserName(txtRegUserName.getText());
			this.appConfig.setRegPassword(txtRegPassword.getText());
			this.appConfig.setWorkMode((byte) (combWorkmode.getSelectedIndex() + 1));
		}
		this.setVisible(false);
	}

}
