import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.com.ha.facecamera.configserver.pojo.CaptureCompareData;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DataViewDialog extends JDialog {

	private static final long serialVersionUID = -1809146545181758377L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_3;
	private JTextField textField_8;
	private CaptureCompareData _data;
	MainForm _mainForm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DataViewDialog dialog = new DataViewDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DataViewDialog(MainForm mainForm) {
		this();
		this._mainForm = mainForm;
	}

	/**
	 * Create the dialog.
	 */
	public DataViewDialog() {
		setTitle("实时数据");
		setBounds(100, 100, 622, 501);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("设备编号：");
		label.setBounds(10, 10, 60, 15);
		contentPanel.add(label);
		
		textField = new JTextField();
		textField.setBounds(80, 7, 66, 21);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("收录时间：");
		label_1.setBounds(10, 41, 60, 15);
		contentPanel.add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(80, 38, 66, 21);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel label_2 = new JLabel("人像质量：");
		label_2.setBounds(10, 72, 60, 15);
		contentPanel.add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setBounds(80, 69, 66, 21);
		contentPanel.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel label_3 = new JLabel("年龄：");
		label_3.setBounds(180, 41, 36, 15);
		contentPanel.add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(226, 38, 66, 21);
		contentPanel.add(textField_3);
		
		JLabel label_4 = new JLabel("性别：");
		label_4.setBounds(180, 72, 36, 15);
		contentPanel.add(label_4);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(226, 69, 66, 21);
		contentPanel.add(textField_4);
		
		JLabel label_5 = new JLabel("现场图片：");
		label_5.setBounds(92, 116, 66, 15);
		contentPanel.add(label_5);
		
		lblNewLabel = new JLabel("无");
		lblNewLabel.setBounds(92, 141, 96, 122);
		contentPanel.add(lblNewLabel);
		
		JLabel label_6 = new JLabel("缩略图：");
		label_6.setBounds(226, 116, 54, 15);
		contentPanel.add(label_6);
		
		lblNewLabel_1 = new JLabel("无");
		lblNewLabel_1.setBounds(226, 141, 96, 122);
		contentPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("人员编号：");
		lblNewLabel_2.setBounds(92, 297, 60, 15);
		contentPanel.add(lblNewLabel_2);
		
		textField_5 = new JTextField();
		textField_5.setBounds(162, 294, 66, 21);
		contentPanel.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel label_7 = new JLabel("人员姓名：");
		label_7.setBounds(92, 325, 60, 15);
		contentPanel.add(label_7);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(162, 322, 66, 21);
		contentPanel.add(textField_6);
		
		JLabel label_8 = new JLabel("相似度：");
		label_8.setBounds(104, 381, 54, 15);
		contentPanel.add(label_8);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(162, 378, 66, 21);
		contentPanel.add(textField_7);
		
		JLabel label_9 = new JLabel("人员角色：");
		label_9.setBounds(92, 353, 60, 15);
		contentPanel.add(label_9);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(162, 350, 66, 21);
		contentPanel.add(textField_8);
		
		lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setBounds(354, 41, 231, 255);
		contentPanel.add(lblNewLabel_3);
		// XXX
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("注册到设备");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(_data == null) {
							return;
						}
						/*if(_data.isPersonMatched()) {
							JOptionPane.showMessageDialog(DataViewDialog.this, "人员已经注册！");
							return;
						}*/
						if(_data.getFeatureImageData() == null) {
							JOptionPane.showMessageDialog(DataViewDialog.this, "抓拍数据未上传人脸特写图！");
							return;							
						}
						if(_mainForm != null)
							_mainForm.addFaceFromCaptureData(_data.getCameraID(), _data.getFeatureImageData());
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	public void showCaptureData(CaptureCompareData data) {
		this._data = data;
		textField.setText("");
		textField_1.setText("");
		textField_2.setText("");
		textField_3.setText("");
		textField_4.setText("");
		textField_5.setText("");
		textField_6.setText("");
		textField_7.setText("");
		textField_8.setText("");
		lblNewLabel.setIcon(null);
		lblNewLabel.setText("无");
		lblNewLabel_1.setIcon(null);
		lblNewLabel_1.setText("无");
		
		if(data.getCameraID() == null || data.getCameraID().isEmpty())
			textField.setText(data.getSn());
		else
			textField.setText(data.getCameraID());
		textField_1.setText(data.getCaptureTime().toString());
		textField_2.setText(String.valueOf(data.getqValue()));
		textField_3.setText(String.valueOf(data.getAge()));
		switch(data.getSex()) {
		case 1:
			textField_4.setText("男");break;
		case 2:
			textField_4.setText("女");break;
			default:
				textField_4.setText("未知");break;
		}
		if(data.isPersonMatched()) {
			textField_5.setText(data.getPersonID());
			textField_6.setText(data.getPersonName());
			textField_7.setText(String.valueOf(data.getMatchScore()));
			switch(data.getPersonRole()) {
			case 0:
				textField_8.setText("普通人员");break;
			case 1:
				textField_8.setText("白名单");break;
			case 2:
				textField_8.setText("黑名单");break;
			}
		}
		lblNewLabel_3.setText("");
		if(data.getIdCardNo() != null) {
			StringBuilder sb = new StringBuilder("<html><body>");
			sb.append(data.getIdCardNo()).append("<br>");
			sb.append(data.getIdCardName()).append("<br>");
			sb.append(data.getIdCardBirth()).append("<br>");
			sb.append(data.getIdCardSex() == 1 ? '男' : '女').append("<br>");
			sb.append(data.getIdCardNation()).append("<br>");
			sb.append(data.getIdCardAddress()).append("<br>");
			sb.append(data.getIdCardOrg()).append("<br>");
			sb.append(data.getIdCardStartDate()).append('-').append(data.getIdCardExpireDate());
			sb.append("</body></html>");
			lblNewLabel_3.setText(sb.toString());
		}
		if(data.getFeatureImageData() != null) {
			try {
				ImageIcon ii = new ImageIcon(zoomImage(ImageIO.read(new ByteArrayInputStream(data.getFeatureImageData())),96,122));
				//ii.setImage(ii.getImage().getScaledInstance(96,122,Image.SCALE_DEFAULT));
				lblNewLabel.setIcon(ii);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(data.getModelImageData() != null) {
			try {
				ImageIcon ii = new ImageIcon(zoomImage(ImageIO.read(new ByteArrayInputStream(data.getModelImageData())),96,122));
				//ii.setImage(ii.getImage().getScaledInstance(96,122,Image.SCALE_DEFAULT));
				lblNewLabel_1.setIcon(ii);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Image zoomImage(BufferedImage bufImg, int w, int h) {
		double wr=0,hr=0;
		Image Itemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);//设置缩放目标图片模板
        
        wr=w*1.0/bufImg.getWidth();     //获取缩放比例
        hr=h*1.0 / bufImg.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(bufImg, null);
        return Itemp;
	}
}
