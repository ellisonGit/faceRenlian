import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.java.com.ha.facecamera.configserver.Constants;
import main.java.com.ha.facecamera.configserver.pojo.Face;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class AddFaceDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 5069397709857639020L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtID;
	private JTextField txtName;
	private JTextField txtWiegand;
	private JTextField txtPicPath;
	private JComboBox<String> combRole;
	private JComboBox<String> combStartTime;
	private JComboBox<String> combEffectTime;
	private DateSelector btnStartTime;
	private DateSelector btnEffectTime;
	private JLabel lblImage;
	private byte[] faceImageData;

	private Face ftu;
	public Face getFaceToUpload() {
		return ftu;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddFaceDialog dialog = new AddFaceDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public AddFaceDialog(byte[] faceImageData) {
		this();
		try {
			lblImage.setIcon(new ImageIcon(zoomImage(ImageIO.read(new ByteArrayInputStream(faceImageData)), 200, 100)));
			this.faceImageData = faceImageData;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddFaceDialog() {
		setBounds(100, 100, 707, 596);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 3));
		{
			JLabel label = new JLabel("人员编号：");
			contentPanel.add(label);
		}
		{
			txtID = new JTextField();
			contentPanel.add(txtID);
			txtID.setColumns(10);
		}
		{
			JLabel label = new JLabel("");
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("人员姓名：");
			contentPanel.add(label);
		}
		{
			txtName = new JTextField();
			contentPanel.add(txtName);
			txtName.setColumns(10);
		}
		{
			JLabel label = new JLabel("");
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("角色：");
			contentPanel.add(label);
		}
		{
			combRole = new JComboBox<String>();
			combRole.addItem("普通人员");
			combRole.addItem("白名单");
			combRole.addItem("黑名单");
			combRole.setSelectedIndex(0);
			contentPanel.add(combRole);
		}
		{
			JLabel label = new JLabel("");
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("模板图片(路径)：");
			contentPanel.add(label);
		}
		{
			txtPicPath = new JTextField();
			contentPanel.add(txtPicPath);
			txtPicPath.setColumns(10);
		}
		{
			JButton button = new JButton("浏览");
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser();
					if(jfc.showOpenDialog(AddFaceDialog.this) == JFileChooser.APPROVE_OPTION) {
						txtPicPath.setText(jfc.getSelectedFile().getPath());
						try {
							faceImageData = Files.readAllBytes(new File(jfc.getSelectedFile().getPath()).toPath());
							lblImage.setIcon(new ImageIcon(zoomImage(ImageIO.read(new ByteArrayInputStream(faceImageData)), 200, 100)));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}						
					}
				}
			});
			contentPanel.add(button);
		}
		{
			JLabel lblNewLabel = new JLabel("模板图片(图片)：");
			contentPanel.add(lblNewLabel);
		}
		{
			lblImage = new JLabel("无");
			contentPanel.add(lblImage);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("(展示已选图片)");
			contentPanel.add(lblNewLabel_2);
		}
		{
			JLabel label = new JLabel("韦根卡号：");
			contentPanel.add(label);
		}
		{
			txtWiegand = new JTextField();
			txtWiegand.setText("0");
			contentPanel.add(txtWiegand);
			txtWiegand.setColumns(10);
		}
		{
			JLabel label = new JLabel("");
			contentPanel.add(label);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("启用日期：");
			contentPanel.add(lblNewLabel_1);
		}
		{
			combStartTime = new JComboBox<String>();
			combStartTime.addItem("立即生效");
			combStartTime.addItem("看右边=>");
			combStartTime.setSelectedIndex(0);
			contentPanel.add(combStartTime);
		}
		{
			btnStartTime = new DateSelector();
			contentPanel.add(btnStartTime);
		}
		{
			JLabel label = new JLabel("有效期止：");
			contentPanel.add(label);
		}
		{
			combEffectTime = new JComboBox<String>();
			combEffectTime.addItem("永久有效");
			combEffectTime.addItem("永久失效");
			combEffectTime.addItem("看右边=>");
			combEffectTime.setSelectedIndex(0);
			contentPanel.add(combEffectTime);
		}
		{
			btnEffectTime = new DateSelector();
			contentPanel.add(btnEffectTime);
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
		if(e.getActionCommand().equals("Cancel"))
			ftu = null;
		else {
			ftu = new Face();
			ftu.setId(txtID.getText());
			ftu.setName(txtName.getText());
			ftu.setRole(combRole.getSelectedIndex());
			//ftu.setJpgFilePath(new String[] {txtPicPath.getText()});
			ftu.setJpgFileContent(new byte[][] {faceImageData});
			ftu.setWiegandNo(Long.parseLong(txtWiegand.getText()));
			if(combStartTime.getSelectedIndex() == 0)
				ftu.setStartDate(Constants.DISABLED);
			else
				ftu.setStartDate(btnStartTime.getDate());
			if(combEffectTime.getSelectedIndex() == 0)
				ftu.setExpireDate(Constants.LONGLIVE);
			else if(combEffectTime.getSelectedIndex() == 1)
				ftu.setExpireDate(Constants.DISABLED);
			else
				ftu.setExpireDate(btnEffectTime.getDate());
		}
		setVisible(false);
	}

}
