/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passman;

import java.awt.CardLayout;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Base64;
import javax.swing.JFrame;
/*import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;*/
import passman.db.SQLiteJDBC;
import passman.model.Model;
import passman.model.PassGenerator;
import passman.db.Crypt;
import passman.model.CryptModel;
import passman.model.ErrorDialog;
import passman.model.User;
//import passman.Utils;

/**
 *
 * @author P057736
 */
public class PassManUI extends javax.swing.JFrame {

    /**
     * Creates new form PassManUI
     */
    public PassManUI() {
        initComponents();
    }
    
    Utils utils = new Utils();

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        popupWindow = new javax.swing.JDialog();
        okPopupWinBtn = new javax.swing.JButton();
        message = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        loginPane = new javax.swing.JPanel();
        addEntryBtn = new javax.swing.JButton();
        viewListBtn = new javax.swing.JButton();
        globalViewPane = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        labelShow = new javax.swing.JTextField();
        usernameShow = new javax.swing.JTextField();
        passwordShow = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        removeEntryBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        commentShow = new javax.swing.JTextArea();
        toggleShowPassword = new javax.swing.JToggleButton();
        addEntryPane = new javax.swing.JPanel();
        newLabel = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        confirmEntryBtn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        newUsername = new javax.swing.JTextField();
        newPassword = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        generateBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        newComment = new javax.swing.JTextArea();
        languagePane = new javax.swing.JPanel();
        portuguese = new javax.swing.JRadioButton();
        english = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        homeBtn = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        addEntry = new javax.swing.JMenuItem();
        viewList = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        languageMenu = new javax.swing.JMenuItem();

        popupWindow.setMinimumSize(new java.awt.Dimension(300, 200));

        okPopupWinBtn.setText("OK");
        okPopupWinBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okPopupWinBtnActionPerformed(evt);
            }
        });

        message.setText("jLabel11");

        javax.swing.GroupLayout popupWindowLayout = new javax.swing.GroupLayout(popupWindow.getContentPane());
        popupWindow.getContentPane().setLayout(popupWindowLayout);
        popupWindowLayout.setHorizontalGroup(
            popupWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popupWindowLayout.createSequentialGroup()
                .addGroup(popupWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(popupWindowLayout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(okPopupWinBtn))
                    .addGroup(popupWindowLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(message)))
                .addContainerGap(235, Short.MAX_VALUE))
        );
        popupWindowLayout.setVerticalGroup(
            popupWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, popupWindowLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(message)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                .addComponent(okPopupWinBtn)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        setMinimumSize(new java.awt.Dimension(400, 345));

        mainPanel.setLayout(new java.awt.CardLayout());

        loginPane.setBackground(new java.awt.Color(51, 163, 252));
        loginPane.setLayout(new java.awt.GridBagLayout());

        addEntryBtn.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("passman/Bundle"); // NOI18N
        addEntryBtn.setText(bundle.getString("ADD")); // NOI18N
        addEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntryBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        loginPane.add(addEntryBtn, gridBagConstraints);

        viewListBtn.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        viewListBtn.setText(bundle.getString("VIEW")); // NOI18N
        viewListBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewListBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        loginPane.add(viewListBtn, gridBagConstraints);

        mainPanel.add(loginPane, "card2");

        jList1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jList1ComponentShown(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        labelShow.setEditable(false);

        usernameShow.setEditable(false);

        passwordShow.setEditable(false);
        passwordShow.setText("************");

        jLabel1.setText(bundle.getString("USERNAME")); // NOI18N

        jLabel5.setText(bundle.getString("PASSWORD")); // NOI18N

        removeEntryBtn.setText(bundle.getString("REMOVE")); // NOI18N
        removeEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeEntryBtnActionPerformed(evt);
            }
        });

        jLabel2.setText(bundle.getString("LABEL")); // NOI18N

        jLabel3.setText(bundle.getString("COMMENTS")); // NOI18N

        commentShow.setEditable(false);
        commentShow.setColumns(20);
        commentShow.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        commentShow.setLineWrap(true);
        commentShow.setRows(5);
        commentShow.setWrapStyleWord(true);
        jScrollPane3.setViewportView(commentShow);

        toggleShowPassword.setSelected(true);
        toggleShowPassword.setText("jToggleButton1");
        toggleShowPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togglePassVisibility(evt);
            }
        });

        javax.swing.GroupLayout globalViewPaneLayout = new javax.swing.GroupLayout(globalViewPane);
        globalViewPane.setLayout(globalViewPaneLayout);
        globalViewPaneLayout.setHorizontalGroup(
            globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(globalViewPaneLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(globalViewPaneLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addGroup(globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, globalViewPaneLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelShow, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, globalViewPaneLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(usernameShow, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, globalViewPaneLayout.createSequentialGroup()
                                .addGroup(globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, globalViewPaneLayout.createSequentialGroup()
                                        .addComponent(passwordShow)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(toggleShowPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                    .addGroup(globalViewPaneLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(removeEntryBtn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        globalViewPaneLayout.setVerticalGroup(
            globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(globalViewPaneLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(removeEntryBtn)
                .addGap(18, 18, 18)
                .addGroup(globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(toggleShowPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(globalViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        mainPanel.add(globalViewPane, "card4");

        newLabel.setMinimumSize(new java.awt.Dimension(100, 25));
        newLabel.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel4.setLabelFor(newUsername);
        jLabel4.setText(bundle.getString("USERNAME")); // NOI18N

        confirmEntryBtn.setText(bundle.getString("CREATE")); // NOI18N
        confirmEntryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmEntryBtnActionPerformed(evt);
            }
        });

        jLabel7.setLabelFor(newPassword);
        jLabel7.setText(bundle.getString("PASSWORD")); // NOI18N

        newUsername.setMinimumSize(new java.awt.Dimension(100, 25));
        newUsername.setPreferredSize(new java.awt.Dimension(100, 25));

        newPassword.setMinimumSize(new java.awt.Dimension(100, 25));
        newPassword.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel9.setText(bundle.getString("LABEL")); // NOI18N

        jLabel10.setText(bundle.getString("COMMENTS")); // NOI18N

        generateBtn.setText(bundle.getString("GENERATE")); // NOI18N
        generateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateBtnActionPerformed(evt);
            }
        });

        newComment.setColumns(20);
        newComment.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        newComment.setLineWrap(true);
        newComment.setRows(4);
        newComment.setWrapStyleWord(true);
        jScrollPane2.setViewportView(newComment);

        javax.swing.GroupLayout addEntryPaneLayout = new javax.swing.GroupLayout(addEntryPane);
        addEntryPane.setLayout(addEntryPaneLayout);
        addEntryPaneLayout.setHorizontalGroup(
            addEntryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addEntryPaneLayout.createSequentialGroup()
                .addGroup(addEntryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(addEntryPaneLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(addEntryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(addEntryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(newPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(newLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(newUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generateBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE))
                    .addGroup(addEntryPaneLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(confirmEntryBtn)))
                .addGap(21, 21, 21))
        );
        addEntryPaneLayout.setVerticalGroup(
            addEntryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addEntryPaneLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(addEntryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addEntryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(newUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(addEntryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(newPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generateBtn))
                .addGroup(addEntryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addEntryPaneLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel10))
                    .addGroup(addEntryPaneLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addComponent(confirmEntryBtn)
                .addContainerGap())
        );

        mainPanel.add(addEntryPane, "card3");

        buttonGroup1.add(portuguese);
        portuguese.setText(bundle.getString("PORTUGUESE")); // NOI18N
        portuguese.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portugueseActionPerformed(evt);
            }
        });

        buttonGroup1.add(english);
        english.setText(bundle.getString("ENGLISH")); // NOI18N
        english.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                englishActionPerformed(evt);
            }
        });

        jLabel6.setText(bundle.getString("BELOW YOU CAN CHOOSE YOUR PREFERRED LANGUAGE.")); // NOI18N
        jLabel6.setMaximumSize(new java.awt.Dimension(200, 14));
        jLabel6.setPreferredSize(new java.awt.Dimension(200, 14));

        jLabel8.setText(bundle.getString("CHANGES WILL ONLY BE VISIBLE AFTER RESTARTING THE APPLICATION.")); // NOI18N

        homeBtn.setText(bundle.getString("BACK")); // NOI18N
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout languagePaneLayout = new javax.swing.GroupLayout(languagePane);
        languagePane.setLayout(languagePaneLayout);
        languagePaneLayout.setHorizontalGroup(
            languagePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(languagePaneLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(languagePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(english)
                    .addComponent(portuguese)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(91, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, languagePaneLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(homeBtn)
                .addContainerGap())
        );
        languagePaneLayout.setVerticalGroup(
            languagePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(languagePaneLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(portuguese)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(english)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                .addComponent(homeBtn)
                .addContainerGap())
        );

        mainPanel.add(languagePane, "card5");

        jMenu1.setText(bundle.getString("FILE")); // NOI18N
        jMenu1.setFont(new java.awt.Font("Arial Narrow", 0, 14)); // NOI18N

        addEntry.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        addEntry.setText(bundle.getString("ADD")); // NOI18N
        addEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntryActionPerformed(evt);
            }
        });
        jMenu1.add(addEntry);

        viewList.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        viewList.setText(bundle.getString("VIEW")); // NOI18N
        viewList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewListActionPerformed(evt);
            }
        });
        jMenu1.add(viewList);

        jMenuBar1.add(jMenu1);

        jMenu2.setText(bundle.getString("EDIT")); // NOI18N
        jMenu2.setFont(new java.awt.Font("Arial Narrow", 0, 14)); // NOI18N

        languageMenu.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        languageMenu.setText(bundle.getString("LANGUAGE")); // NOI18N
        languageMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageMenuActionPerformed(evt);
            }
        });
        jMenu2.add(languageMenu);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * This is triggered when the user clicks the "Add" button.
     * @param evt 
     */
    private void addEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryActionPerformed
        CardLayout card = (CardLayout)mainPanel.getLayout();
        card.show(mainPanel, "card3");
        //removeEntryBtn.setEnabled(false);
    }//GEN-LAST:event_addEntryActionPerformed

    /**
     * This is triggered when the user clicks the "Create" button.
     * @param evt 
     */
    private void confirmEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmEntryBtnActionPerformed
        // Get values from text fields
        String label = newLabel.getText();
        String username = newUsername.getText();
        String plainTextPassword = newPassword.getText();
        String comment = newComment.getText();
        // Calls method that encrypts the password using the current user values and saves entry to DB
        Utils.addToDBFromUI(label, username, plainTextPassword, comment);
    }//GEN-LAST:event_confirmEntryBtnActionPerformed

    private void jList1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jList1ComponentShown

    }//GEN-LAST:event_jList1ComponentShown

    /**
     * This is triggered when the user changes his list selection
     * @param evt 
     */
    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        if(jList1.getSelectedIndex()>-1){
            // convert jList1 item to a Model item
            Model thisModel = (Model)jList1.getSelectedValue();
            System.out.println("[MODEL]Encrypted password size: "+thisModel.getPassword().length + " bytes");
            //System.out.println("[]Encrypted password size: "+thisModel.getPassword().length + " bytes");
            // shows label, username and comments
            labelShow.setText(thisModel.getLabel());
            usernameShow.setText(thisModel.getUsername());
            commentShow.setText(thisModel.getComment());
            
            // only shows password if toggle is selected
            if(toggleShowPassword.isSelected()){
                String plainTextPassword = Utils.getFromDBToUI(thisModel.getPassword());
                
                passwordShow.setText(plainTextPassword);
            }
            // Enable remove button
            removeEntryBtn.setEnabled(true);
        }
        else{
            // Disable remove button
            removeEntryBtn.setEnabled(false);
        }
    }//GEN-LAST:event_jList1ValueChanged

    private void viewListBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewListBtnActionPerformed
        
        utils.refreshView(jList1, mainPanel);
        
        removeEntryBtn.setEnabled(false);
        
    }//GEN-LAST:event_viewListBtnActionPerformed

    private void addEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryBtnActionPerformed
        CardLayout card = (CardLayout)mainPanel.getLayout();
        card.show(mainPanel, "card3");
    }//GEN-LAST:event_addEntryBtnActionPerformed

    private void viewListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewListActionPerformed
        utils.refreshView(jList1, mainPanel);
    }//GEN-LAST:event_viewListActionPerformed

    private void removeEntryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeEntryBtnActionPerformed
        Model model = (Model)jList1.getSelectedValue();
        
        SQLiteJDBC sqlite = new SQLiteJDBC();
        sqlite.removeItem(model);
        
        utils.refreshView(jList1, mainPanel);
        jList1.setSelectedIndex(0);
    }//GEN-LAST:event_removeEntryBtnActionPerformed

    private void languageMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_languageMenuActionPerformed
        if(Utils.loadParams().get(1).equals("pt")){ //NOI18N
            portuguese.setSelected(true);
        }
        else {
            english.setSelected(true);
        }
        CardLayout card = (CardLayout)mainPanel.getLayout();
        card.show(mainPanel, "card5"); //NOI18N
    }//GEN-LAST:event_languageMenuActionPerformed

    private void portugueseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portugueseActionPerformed
        utils.saveParamChanges("pt", "PT"); //NOI18N
    }//GEN-LAST:event_portugueseActionPerformed

    private void englishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_englishActionPerformed
        utils.saveParamChanges("en", "UK"); //NOI18N
    }//GEN-LAST:event_englishActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        CardLayout card = (CardLayout)mainPanel.getLayout();
        card.show(mainPanel, "card2"); //NOI18N
    }//GEN-LAST:event_homeBtnActionPerformed

    private void generateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateBtnActionPerformed
        newPassword.setText(PassGenerator.generate());        
    }//GEN-LAST:event_generateBtnActionPerformed

    private void okPopupWinBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okPopupWinBtnActionPerformed
        // disposes popup window
        popupWindow.dispose();
    }//GEN-LAST:event_okPopupWinBtnActionPerformed

    /**
     * This is triggered when the user toggles the password visibility
     * @param evt 
     */
    private void togglePassVisibility(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togglePassVisibility
        
    }//GEN-LAST:event_togglePassVisibility

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (java.util.ResourceBundle.getBundle("passman/Bundle").getString("NIMBUS").equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PassManUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PassManUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PassManUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PassManUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Set location (language) according to user preferences
        Locale.setDefault(new Locale(Utils.loadParams().get(1),Utils.loadParams().get(0)));
        
        
        // Creates DB if it doesn't exist
        Utils.verifyDB();
        
        // Creates hash pass and user
        ArrayList<byte[]> list = Crypt.getSecurePassword("Tfr5Jbv33");
        User user = new User("admin", list.get(0), list.get(1));
        
        // the two outputs should be true
        //System.out.println(Arrays.equals(list.get(0), user.getSecurePassword()));
        //System.out.println(Arrays.equals(list.get(1), user.getSaltArray()));
        
        // adds user to DB
        SQLiteJDBC sqlite = new SQLiteJDBC();
        sqlite.addUser(user);
        
        // gets user from DB
        User newUser = sqlite.getUser("admin");
        
        // these two outputs should be true
        //System.out.println(Arrays.equals(newUser.getSecurePassword(), newUser.getSecurePassword()));
        //System.out.println(Arrays.equals(newUser.getSaltArray(), newUser.getSaltArray()));
        
        // encrypt message
        byte[] input = "Mensagem para encriptar".getBytes(StandardCharsets.UTF_8);
        CryptModel encObj = Crypt.encrypt(newUser.getSecurePassword(), newUser.getSaltArray(), input);
        
        // decrypt message
        System.out.println("Encrypted size MAIN: " + encObj.encryptedPassword.length);
        byte[] output = Crypt.decrypt(newUser.getSecurePassword(), newUser.getSaltArray(), encObj.encryptedPassword);
        
        // this output should be true
        System.out.println(Arrays.equals(input, output));
        
        // Convert output to string again
        String outputStr = new String(output,StandardCharsets.UTF_8);
        System.out.println(outputStr);
        
        // this output should also be true
        System.out.println("Mensagem para encriptar".equals(outputStr));
        
        // save encrypted message to DB
        Model entry = new Model("teste","user",encObj.encryptedPassword,newUser.getSaltArray(),null);
        sqlite.addItem(entry);
        
        // get message from DB
        Model retrievedEntry = sqlite.getItem("teste");
        
        // this output should be true
        System.out.println(Arrays.equals(encObj.encryptedPassword, retrievedEntry.getPassword()));
        
        // decrypt received message
        byte[] outputFromDB = Crypt.decrypt(newUser.getSecurePassword(), newUser.getSaltArray(), retrievedEntry.getPassword());
        
        // Convert to String
        String outputStrFromDB = new String(outputFromDB,StandardCharsets.UTF_8);
        
        System.out.println(outputStrFromDB);
        
        // this output should still be true
        System.out.println("Mensagem para encriptar".equals(outputStrFromDB));
        
         /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PassManUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addEntry;
    private javax.swing.JButton addEntryBtn;
    private javax.swing.JPanel addEntryPane;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextArea commentShow;
    private javax.swing.JButton confirmEntryBtn;
    private javax.swing.JRadioButton english;
    private javax.swing.JButton generateBtn;
    private javax.swing.JPanel globalViewPane;
    private javax.swing.JButton homeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField labelShow;
    private javax.swing.JMenuItem languageMenu;
    private javax.swing.JPanel languagePane;
    private javax.swing.JPanel loginPane;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel message;
    private javax.swing.JTextArea newComment;
    private javax.swing.JTextField newLabel;
    private javax.swing.JTextField newPassword;
    private javax.swing.JTextField newUsername;
    private javax.swing.JButton okPopupWinBtn;
    private javax.swing.JTextField passwordShow;
    private javax.swing.JDialog popupWindow;
    private javax.swing.JRadioButton portuguese;
    private javax.swing.JButton removeEntryBtn;
    private javax.swing.JToggleButton toggleShowPassword;
    private javax.swing.JTextField usernameShow;
    private javax.swing.JMenuItem viewList;
    private javax.swing.JButton viewListBtn;
    // End of variables declaration//GEN-END:variables
}
