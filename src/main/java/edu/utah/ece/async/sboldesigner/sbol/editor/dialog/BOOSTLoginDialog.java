package edu.utah.ece.async.sboldesigner.sbol.editor.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import edu.utah.ece.async.sboldesigner.sbol.editor.SBOLEditorPreferences;
import edu.utah.ece.async.sboldesigner.swing.FormBuilder;
import edu.utah.ece.async.sboldesigner.versioning.PersonInfo;


public class BOOSTLoginDialog extends JDialog implements ActionListener {

	    //private BOOSTClient mBOOSTClient = null;
		private Component parent;

		private final JButton loginButton = new JButton("Login");
		private final JButton cancelButton = new JButton("Cancel");
		private final JTextField username = new JTextField("");
		private final JPasswordField password = new JPasswordField("");
		private final JButton signUpButton = new JButton("Sign Up");

		public BOOSTLoginDialog(Component parent) {
			super(JOptionPane.getFrameForComponent(parent), "Login to BOOST", true);
			this.parent = parent;
			
			PersonInfo userInfo = SBOLEditorPreferences.INSTANCE.getUserInfo();
			String email = userInfo == null || userInfo.getEmail() == null ? null : userInfo.getEmail().getLocalName();
			username.setText(email);
			password.setEchoChar('*');

			cancelButton.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
					JComponent.WHEN_IN_FOCUSED_WINDOW);
			cancelButton.addActionListener(this);

			signUpButton.addActionListener(this);
			loginButton.addActionListener(this);
			getRootPane().setDefaultButton(loginButton);

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
			buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			buttonPane.add(Box.createHorizontalStrut(100));
			buttonPane.add(Box.createHorizontalGlue());
			buttonPane.add(cancelButton);
			buttonPane.add(signUpButton);
			buttonPane.add(loginButton);
		
			FormBuilder builder = new FormBuilder();
			builder.add("Username", username);
			builder.add("Password", password);
			JPanel mainPanel = builder.build();
			mainPanel.setAlignmentX(LEFT_ALIGNMENT);

			JLabel infoLabel = new JLabel(
					"Login to BOOST account.  This enables you to optimise your genetic constructs."
					 +" If you do not have BOOST account, please opt for Sign Up");

			Container contentPane = getContentPane();
			contentPane.add(infoLabel, BorderLayout.PAGE_START);
			contentPane.add(mainPanel, BorderLayout.CENTER);
			contentPane.add(buttonPane, BorderLayout.PAGE_END);
			((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
				try {
					String urlString = "https://contacts.jgi.doe.gov/registration/new";
			        openWebpage(new URL(urlString).toURI());
			    } catch (URISyntaxException error) {
			        error.printStackTrace();
			    } catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
			}

			if (e.getSource() == loginButton) {
				try {
					//TODO: Login related code here	
					
				} catch (Exception e1) {
					setVisible(false);
					e1.printStackTrace();
					return;
				}
				setVisible(false);
				return;
			}
		}
		
		private void openWebpage(java.net.URI uri) {
		    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		        try {
		            desktop.browse(uri);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}
}