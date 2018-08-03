package edu.utah.ece.async.sboldesigner.boost;

import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.sbolstandard.core2.SBOLDocument;

import edu.utah.ece.async.sboldesigner.sbol.editor.dialog.DialogUtils;
import edu.utah.ece.async.sboldesigner.sbol.editor.dialog.MessageDialog;
import edu.utah.ece.async.sboldesigner.swing.FormBuilder;
import gov.doe.jgi.boost.client.BOOSTClient;
import gov.doe.jgi.boost.client.utils.UIUtils;


public class BOOSTLoginDialog extends JDialog implements ActionListener {

	    private BOOSTClient mBOOSTClient = null;
	    private SBOLDocument currentDesign= null;
		private Component parent;

		private final JButton loginButton = new JButton("Login");
		private final JButton cancelButton = new JButton("Cancel");
		private final JTextField username = new JTextField("");
		private final JPasswordField password = new JPasswordField("");
		private final JButton signUpButton = new JButton("Sign Up");

		public BOOSTLoginDialog(Component parent, SBOLDocument doc) {
			super(JOptionPane.getFrameForComponent(parent), "Login to BOOST", true);
			this.parent = parent;
			this.currentDesign = doc;
			
			new DialogUtils(username, password);
			DialogUtils.setUserInfo();
			
			cancelButton.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
					        JComponent.WHEN_IN_FOCUSED_WINDOW);
			cancelButton.addActionListener(this);
			signUpButton.addActionListener(this);
			loginButton.addActionListener(this);
			getRootPane().setDefaultButton(loginButton);

			JPanel buttonPane = DialogUtils.buildDecisionArea(0); // 0 for LINE_AXIS alignment
			buttonPane.add(cancelButton);
			buttonPane.add(signUpButton);
			buttonPane.add(loginButton);

			FormBuilder builder = DialogUtils.initBuilder();
			JPanel mainPanel = builder.build();
			mainPanel.setAlignmentX(LEFT_ALIGNMENT);

			JLabel infoLabel = new JLabel(
					"Login to BOOST account.  This enables you to optimise your genetic constructs."
					 +" If you do not have BOOST account, please opt for Sign Up");
			
			Container contentPane = getContentPane();
			DialogUtils.setUI(contentPane, infoLabel, mainPanel, buttonPane);
			pack();
			setLocationRelativeTo(parent);
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cancelButton) {
				setVisible(false);
				return;
			}else if(e.getSource() == signUpButton) {
				String urlString = "https://contacts.jgi.doe.gov/registration/new";
				UIUtils.openWebPage(urlString);
			}

			if (e.getSource() == loginButton) {
				try {
					// Login related code here
					mBOOSTClient = new BOOSTClient(username.getText(), new String(password.getPassword()));
					String mJWTToken = mBOOSTClient.getToken();
					if(mJWTToken != null && !mJWTToken.isEmpty()) {
						new BOOSTPreferences().setBOOSTToken(mJWTToken);
					}
					setVisible(false);
					if(null != mJWTToken) {
						JOptionPane.showMessageDialog(parent, "Login successful!");
					}	
				new AvailableOperationsDialog(parent, currentDesign);
				} catch (Exception e1) {
					setVisible(false);
					MessageDialog.showMessage(parent, "Login failed", Arrays.asList(e1.getMessage().split("\"|,")));
					mBOOSTClient = null;
					e1.printStackTrace();
					return;
				} 
				return;
			}
		}
}