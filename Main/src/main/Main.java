package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;

    public Main() {
        setTitle("Registracija");
        setSize(450, 415);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        Color bojaPozadine = Color.BLUE;
        getContentPane().setBackground(bojaPozadine);

        JLabel registrationLabel = new JLabel("WorldAdventure");
        registrationLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
        registrationLabel.setBounds(121, 26, 200, 40);
        registrationLabel.setBackground(Color.GREEN); 
        registrationLabel.setOpaque(true);
        registrationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(registrationLabel);

        JLabel usernameLabel = new JLabel("USERNAME");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        usernameLabel.setForeground(new Color(255, 255, 255));
        usernameLabel.setBounds(25, 76, 143, 40);
        getContentPane().add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(25, 111, 143, 40);
        getContentPane().add(usernameField);

        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passwordLabel.setForeground(new Color(255, 255, 255));
        passwordLabel.setBounds(25, 160, 143, 40);
        getContentPane().add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(25, 190, 143, 40);
        getContentPane().add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(87, 260, 100, 40);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        getContentPane().add(registerButton);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(262, 260, 100, 40);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        getContentPane().add(loginButton);

        JButton openWorldsButton = new JButton("Lista Svijetova");
        openWorldsButton.setBounds(151, 310, 143, 40);
        openWorldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWorldsDialog();
            }
        });
        getContentPane().add(openWorldsButton);

        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Dodatne informacije o liku
        JTextField nameField = new JTextField();
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"M", "F"});
        JComboBox<String> hairColorComboBox = new JComboBox<>(new String[]{"Black", "Brown", "Blonde", "Red", "White", "Gray", "Blue", "Green", "Purple", "Pink"});
        JComboBox<String> eyeColorComboBox = new JComboBox<>(new String[]{"Brown", "Blue", "Green", "Hazel", "Gray", "Amber", "Black"});
        JComboBox<String> skinColorComboBox = new JComboBox<>(new String[]{"Light", "Fair", "Medium", "Olive", "Brown", "Dark", "Ebony"});

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderComboBox);
        panel.add(new JLabel("Hair Color:"));
        panel.add(hairColorComboBox);
        panel.add(new JLabel("Eye Color:"));
        panel.add(eyeColorComboBox);
        panel.add(new JLabel("Skin Color:"));
        panel.add(skinColorComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Character Information", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String gender = (String) genderComboBox.getSelectedItem();
            String hairColor = (String) hairColorComboBox.getSelectedItem();
            String eyeColor = (String) eyeColorComboBox.getSelectedItem();
            String skinColor = (String) skinColorComboBox.getSelectedItem();

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr:3306/isvalina", "isvalina", "11");
                PreparedStatement statement = connection.prepareStatement("INSERT INTO tablica1 (username, password, ime_lik, spol, boja_kose, boja_ociju, boja_koze) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, name);
                statement.setString(4, gender);
                statement.setString(5, hairColor);
                statement.setString(6, eyeColor);
                statement.setString(7, skinColor);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error during registration");
                }
                connection.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void openWorldsDialog() {
        DefaultListModel<String> worldList = new DefaultListModel<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr:3306/isvalina", "isvalina", "11");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT WorldName FROM World");
            while (resultSet.next()) {
                String worldName = resultSet.getString("WorldName");
                worldList.addElement(worldName);
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        JList<String> worldJList = new JList<>(worldList);
        worldJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(worldJList);
        scrollPane.setPreferredSize(new Dimension(200, 200));

        int result = JOptionPane.showOptionDialog(null, scrollPane, "Select World",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (result == JOptionPane.OK_OPTION) {
            String selectedWorld = worldJList.getSelectedValue();
            if (selectedWorld != null) {
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr:3306/isvalina", "isvalina", "11");
                    PreparedStatement statement = connection.prepareStatement("SELECT ApocalypseDescription FROM World WHERE WorldName = ?");
                    statement.setString(1, selectedWorld);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String apocalypseDescription = resultSet.getString("ApocalypseDescription");
                        JOptionPane.showMessageDialog(null, "World: " + selectedWorld + "\nApocalypse Description: " + apocalypseDescription);
                    }
                    connection.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        }
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr:3306/isvalina", "isvalina", "11");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tablica1 WHERE username = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
            	
                String characterName = resultSet.getString("ime_lik");
                openGameWindow(characterName);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    //feedback razine
    private void saveFeedbackToDatabase(String feedback) {
        String url = "jdbc:mysql://ucka.veleri.hr:3306/isvalina";
        String user = "isvalina";
        String password = "11";
        String sql = "INSERT INTO Feedback (feedbackText) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, feedback);
            pstmt.executeUpdate();
            System.out.println("Hvala vam na povratnoj informaciji.");
        } catch (SQLException ex) {
            System.out.println("Greška prilikom spremanja informacija: " + ex.getMessage());
        }
    }
    

    private void openGameWindow(String characterName) {
        JFrame gameFrame = new JFrame("World Adventure");
        gameFrame.setSize(800, 600); // Povećavamo veličinu prozora
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.getContentPane().setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(0, 1)); // Panel za gumbi svjetova
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://ucka.veleri.hr:3306/isvalina", "isvalina", "11");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT WorldName FROM World");
            while (resultSet.next()) {
                String worldName = resultSet.getString("WorldName");
                JButton worldButton = new JButton(worldName); // Stvaramo gumb za svaki svijet
                worldButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton clickedButton = (JButton)e.getSource(); // Dobivanje gumba koji je pritisnut
                        String selectedWorld = clickedButton.getText(); // Dobivanje naziva odabranog svijeta

                        //SVIJET VATRE
                        if (selectedWorld.equals("Svijet Vatre")) {
                            JFrame worldFrame = new JFrame(selectedWorld);
                            worldFrame.setSize(600, 400); // Povećavamo veličinu prozora za prikaz svijeta
                            worldFrame.getContentPane().setLayout(new BorderLayout());

                            JLabel worldTitleLabel = new JLabel(selectedWorld);
                            worldTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            worldTitleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Povećavamo veličinu fonta
                            worldFrame.getContentPane().add(worldTitleLabel, BorderLayout.NORTH);
                            

                            // Dodajemo tekst iznad gumba
                            JLabel customTextLabel = new JLabel("<html><div style='text-align: center;'>Nalazite se u Svijetu Vatre. Ispred vas je Vulkan koji eruptira i Šuma vatrenih borova.</div></html>");
                            customTextLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centriranje teksta
                            worldFrame.getContentPane().add(customTextLabel, BorderLayout.CENTER);

                            // Dodajemo panele za gumbe
                            JPanel buttonPanel = new JPanel(new GridLayout(3, 1)); // 3 reda, 1 stupac
                            JPanel buttonPanelRow1 = new JPanel(); // Dodani panel za poravnanje buttona
                            JPanel buttonPanelRow2 = new JPanel(); // Dodani panel za poravnanje buttona

                            JButton button1 = new JButton("Prema šumi vatrenih borova"); // Dodajemo tekst za prvi button
                            JButton button2 = new JButton("Približiti se Vulkanu"); // Dodajemo tekst za drugi button

                            // Dodajemo prazan prostor između prvog i drugog buttona (još veći)
                            buttonPanelRow1.add(button1);
                            buttonPanelRow1.add(Box.createHorizontalStrut(100)); // Dodajemo prazan prostor između gumba
                            buttonPanelRow1.add(button2);

                            // Dodajemo prazan prostor ispod prvog i drugog reda gumba
                            buttonPanelRow2.add(Box.createVerticalStrut(40)); // Dodajemo prazan prostor

                            buttonPanel.add(buttonPanelRow1);
                            buttonPanel.add(buttonPanelRow2); // Dodajemo panel s trećim buttonom ispod prvog i drugog
                            worldFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                            

                            // Dodajemo ActionListener za button "Približiti se Vulkanu"/"Nastaviti istraživati vulkan"
                            button2.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (button2.getText().equals("Približiti se Vulkanu")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Približili ste se vulkanu. Lava teče niz padinu, a vrućina postaje neizdrživa. Što ćete dalje?</div></html>");
                                        button1.setText("Povratak nazad");
                                        button2.setText("Nastaviti istraživati vulkan");
                                    } else if (button2.getText().equals("Nastaviti istraživati vulkan")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Nastavljate istraživati vulkan. Nalazite ulaz u tajnu pećinu ispod lave. Što ćete dalje?</div></html>");
                                        button1.setText("Vratiti se");
                                        button2.setText("Ući u pećinu");
                                    } else if (button2.getText().equals("Ući u pećinu")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Boss Fight");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Borite se protiv opasnog zmaja!");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("Ashborn, king of dragons");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Health: 3");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Napad");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {3};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Health: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Porazili ste zmaja i izašli iz Svijeta Vatre.");
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                    } else if (button2.getText().equals("Nastaviti dalje u šumu")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Nailazite na hram okružen vatrom hoće te li ući?</div></html>");
                                        button1.setText("Krenite nazad");
                                        button2.setText("Ući u hram");
                                    	
                                    } else if (button2.getText().equals("Ući u hram")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Boss Fight");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Borite se protiv zlog čarobnjaka!");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("Querehsha, Fire Monarch");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Health: 5");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Napad");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {5};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Health: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Porazili zlog čarobnjaka izašli iz Svijeta Vatre.");
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                }
                                }
                            });
                            
                            button1.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (button1.getText().equals("Prema šumi vatrenih borova")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Nalazite se ispred šume što ćete dalje?</div></html>");
                                        button1.setText("Povratak nazad");
                                        button2.setText("Nastaviti dalje u šumu");
                                    } else if (button1.getText().equals("Povratak nazad")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Nalazite se u Svijetu Vatre. Ispred vas je Vulkan koji eruptira i Šuma vatrenih borova.</div></html>");
                                        button1.setText("Prema šumi vatrenih borova");
                                        button2.setText("Približiti se Vulkanu");
                                    } else if (button1.getText().equals("Krenite nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Nalazite se ispred šume što ćete dalje?</div></html>");
                                        button1.setText("Povratak nazad");
                                        button2.setText("Nastaviti dalje u šumu");
                                    	
                                    }
                                }
                            });
                                    	
                          
                            worldFrame.setVisible(true);
                            
                            //SVIJET LEDENIH OBLAKA
                        } else if (selectedWorld.equals("Svijet Ledenih Oblaka")) {
                        	JFrame worldFrame = new JFrame(selectedWorld);
                            worldFrame.setSize(600, 400); // Povećavamo veličinu prozora za prikaz svijeta
                            worldFrame.getContentPane().setLayout(new BorderLayout());

                            JLabel worldTitleLabel = new JLabel(selectedWorld);
                            worldTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            worldTitleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Povećavamo veličinu fonta
                            worldFrame.getContentPane().add(worldTitleLabel, BorderLayout.NORTH);

                            // Dodajemo tekst iznad gumba
                            JLabel customTextLabel = new JLabel("<html><div style='text-align: center;'>Nalazite se u Svijetu Ledenih Oblaka. Ispred vas je Tunel kroz ledenu santu i Smrznuto jezero.</div></html>");
                            customTextLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centriranje teksta
                            worldFrame.getContentPane().add(customTextLabel, BorderLayout.CENTER);

                            // Dodajemo panele za gumbe
                            JPanel buttonPanel = new JPanel(new GridLayout(3, 1)); // 3 reda, 1 stupac
                            JPanel buttonPanelRow1 = new JPanel(); // Dodani panel za poravnanje buttona
                            JPanel buttonPanelRow2 = new JPanel(); // Dodani panel za poravnanje buttona

                            JButton button1 = new JButton("Preko smrznutog jezera"); // Dodajemo tekst za prvi button
                            JButton button2 = new JButton("Prema tunelu"); // Dodajemo tekst za drugi button

                            // Dodajemo prazan prostor između prvog i drugog buttona (još veći)
                            buttonPanelRow1.add(button1);
                            buttonPanelRow1.add(Box.createHorizontalStrut(100)); // Dodajemo prazan prostor između gumba
                            buttonPanelRow1.add(button2);

                            // Dodajemo prazan prostor ispod prvog i drugog reda gumba
                            buttonPanelRow2.add(Box.createVerticalStrut(40)); // Dodajemo prazan prostor

                            buttonPanel.add(buttonPanelRow1);
                            buttonPanel.add(buttonPanelRow2); // Dodajemo panel s trećim buttonom ispod prvog i drugog
                            worldFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                            

                            
                            button2.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (button2.getText().equals("Prema tunelu")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Ispred tunela vam nepoznati stranac vam govori da ne ulazite, hoćete li ući?</div></html>");
                                        button1.setText("Povratak nazad");
                                        button2.setText("Nastaviti kroz tunel");
                                    } else if (button2.getText().equals("Nastaviti kroz tunel")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>U tunelu nailazite na skeleton prošlih avanturista i pored njih mač, uzmite ga kako bi nastavili dalje.</div></html>");
                                        button1.setText("Povrat nazad");
                                        button2.setText("Uzmi mač");
                                    } else if (button2.getText().equals("Uzmi mač")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Nailazite na vrata u magli. Želite li ući kroz vrata?</div></html>");
                                        button1.setText("Vratit se nazad");
                                        button2.setText("Ući kroz vrata");
                                    }else if (button2.getText().equals("Ući kroz vrata")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Boss Fight");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Borite se protiv vođe ledenih demona!");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("Antares, Ice Demon");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Health: 15");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Napad");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {15};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Health: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Porazili ste vođu demona i oslobodili svijet od vječne zime.");
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                        
                                        //drugi svijet ledenih
                                    } else if (button2.getText().equals("Krenuti preko jezera")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Prije jezera nailazite na knjigu sa drevnim runama, uzmite je da nastavite dalje</div></html>");
                                        button1.setText("Nazad");
                                        button2.setText("Uzmite knjigu");
                                    	
                                    } else if (button2.getText().equals("Uzmite knjigu")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Dok idete preko jezera led počne da puca, krenete da padate u vodu no knjiga koju ste uzeli ima čarobne moći i pomoću nje zaledite cijelo jezere debelim slojem leda. Na kraju jezera vas čeka prazan tron usred ničega.</div></html>");
                                        button1.setText("Vratite se");
                                        button2.setText("Priđite tronu");
                                    	
                                    } else if (button2.getText().equals("Priđite tronu")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Boss Fight");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Borite se protiv ledenog diva");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("Ymir, Frost Giant");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Health: 20");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Napad");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {20};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Health: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Porazili ste ledenog diva.");
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                }
                                }
                            });
                            
                            button1.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (button1.getText().equals("Preko smrznutog jezera")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Dolazite do jezera ali morate preko njega. Što će te učiniti?</div></html>");
                                        button1.setText("Vratiti se nazad");
                                        button2.setText("Krenuti preko jezera");
                                    } else if (button1.getText().equals("Povratak nazad")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Nalazite se u Svijetu Ledenih Oblaka. Ispred vas je Tunel kroz ledenu santu i Smrznuto jezero.</div></html>");
                                        button1.setText("Preko smrznutog jezera");
                                        button2.setText("Prema tunelu");
                                    } else if (button1.getText().equals("Povrat nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Ispred tunela vam nepoznati stranac vam govori da ne ulazite, hoćete li ući?</div></html>");
                                        button1.setText("Povratak nazad");
                                        button2.setText("Nastaviti kroz tunel");
                                    	
                                    }else if (button1.getText().equals("Vratiti se nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Nalazite se u Svijetu Ledenih Oblaka. Ispred vas je Tunel kroz ledenu santu i Smrznuto jezero.</div></html>");
                                        button1.setText("Preko smrznutog jezera");
                                        button2.setText("Prema tunelu");
                                    	
                                    } else if (button1.getText().equals("Nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Dolazite do jezera ali morate preko njega. Što će te učiniti?</div></html>");
                                        button1.setText("Vratiti se nazad");
                                        button2.setText("Krenuti preko jezera");
                                    	
                                    } else if (button1.getText().equals("Vratite se")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Prije jezera nailazite na knjigu sa drevnim runama, uzmite je da nastavite dalje</div></html>");
                                        button1.setText("Nazad");
                                        button2.setText("Uzmite knjigu");
                                    	
                                    }
                                }
                            });
                                    	
                          
                            worldFrame.setVisible(true);
                        
                            //SVIJET BILJNIH ZOMBIJA
                        } else if (selectedWorld.equals("Svijet Biljnih Zombija")) {
                        	JFrame worldFrame = new JFrame(selectedWorld);
                            worldFrame.setSize(600, 400); // Povećavamo veličinu prozora za prikaz svijeta
                            worldFrame.getContentPane().setLayout(new BorderLayout());

                            JLabel worldTitleLabel = new JLabel(selectedWorld);
                            worldTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            worldTitleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Povećavamo veličinu fonta
                            worldFrame.getContentPane().add(worldTitleLabel, BorderLayout.NORTH);

                            // Dodajemo tekst iznad gumba
                            JLabel customTextLabel = new JLabel("<html><div style='text-align: center;'>Ušli ste zapušten svijet pun vegetacije. Ispred vas su 2 puta jedan vodi do močvare a drugi vodi do velikog labirinta trnja</div></html>");
                            customTextLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centriranje teksta
                            worldFrame.getContentPane().add(customTextLabel, BorderLayout.CENTER);

                            // Dodajemo panele za gumbe
                            JPanel buttonPanel = new JPanel(new GridLayout(3, 1)); // 3 reda, 1 stupac
                            JPanel buttonPanelRow1 = new JPanel(); // Dodani panel za poravnanje buttona
                            JPanel buttonPanelRow2 = new JPanel(); // Dodani panel za poravnanje buttona

                            JButton button1 = new JButton("Prema močvari"); // Dodajemo tekst za prvi button
                            JButton button2 = new JButton("Prema labirintu trnja"); // Dodajemo tekst za drugi button

                            // Dodajemo prazan prostor između prvog i drugog buttona (još veći)
                            buttonPanelRow1.add(button1);
                            buttonPanelRow1.add(Box.createHorizontalStrut(100)); // Dodajemo prazan prostor između gumba
                            buttonPanelRow1.add(button2);

                            // Dodajemo prazan prostor ispod prvog i drugog reda gumba
                            buttonPanelRow2.add(Box.createVerticalStrut(40)); // Dodajemo prazan prostor

                            buttonPanel.add(buttonPanelRow1);
                            buttonPanel.add(buttonPanelRow2); // Dodajemo panel s trećim buttonom ispod prvog i drugog
                            worldFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                            

                            // Dodajemo ActionListener za button2
                            button2.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (button2.getText().equals("Prema labirintu trnja")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Prilazite ulazu u labirint. Ispred ulaza je znak upozorenja da ne ulazite u labirint. Hoće te li ući?</div></html>");
                                        button1.setText("Povratak nazad");
                                        button2.setText("Ignoriraj upozorenje i uđi u labirint");
                                    } else if (button2.getText().equals("Ignoriraj upozorenje i uđi u labirint")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Duboko ste u labirintu ne možete više nazad. Ispred vas su 2 puta sa znakom iznad na jednom piše 'NE ULAZI' a na drugom 'VELIKO BOGATSTVO'</div></html>");
                                        button1.setText("Ući na znak 'NE ULAZI'");
                                        button2.setText("Ući na znak 'VELIKO BOGATSVO'");
                                    } else if (button2.getText().equals("Ući na znak 'VELIKO BOGATSVO'")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Znak vas nije lagao nailazite na škrinju sa zlatom no u prostoru nema izlaza morate se vratiti.</div></html>");
                                        button1.setText("Izađite van");
                                        button2.setText("Izađite van");
                                    } else if (button2.getText().equals("Izađite van")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Izgleda da je jedina nada da izađete iz labirinta da uđete na put 'NE ULAZI'</div></html>");
                                        button1.setText("Ući na znak 'NE ULAZI'");
                                        button2.setText("Uzeli ste već blago");
                                        
                                 
                                        //močvara
                                    } else if (button2.getText().equals("Ući dublje u močvaru")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Ušli ste dublje u močvaru i počnete halucinirat. Ispred vas vidite čovjeka koji se čini prijateljski, izgleda da vas zove da dođete.</div></html>");
                                        button1.setText("Nazad");
                                        button2.setText("Ignoriraj čovjeka i nastavi dalje");
                                    	
                                    
                                        
                                        
                                    } else if (button2.getText().equals("Ignoriraj čovjeka i nastavi dalje")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Izgleda da je čovjek bio halucinacija, no odjednom halucinacije prestanu i pred vama se pojave dva čudovišta od blata</div></html>");
                                        button1.setText("Idite nazad");
                                        button2.setText("Napadnite čudovišta");
                                    	
                                    
                                        
                                        
                                    } else if (button2.getText().equals("Napadnite čudovišta")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Nakon dugačke borbe jedva ste uspjeli poraziti čudovišta tako što ste ih isjekli mačem. Odjedom se pojavi cijela skupina čudovišta i spoje se u jedno veliko čudovište. Morate ga porazit da izađete iz močvare</div></html>");
                                        button1.setText("U napad");
                                        button2.setText("U napad");
                                    	
                                    
                                        
                                        
                                    }else if (button2.getText().equals("U napad")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Boss Fight");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Borite se protiv čudovišta od blata");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("Swamphon, Mud golem");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Health: 30");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Napad");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {30};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Health: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Porazili čudovište od blata i izašli iz močvare.");
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                        
                                        
                                        //labirint
                                } else if (button2.getText().equals("Ući na znak 'VELIKO BOGATSVO'")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Znak vas nije lagao nailazite na škrinju sa zlatom no u prostoru nema izlaza morate se vratiti.</div></html>");
                                        button1.setText("Izađite van");
                                        button2.setText("Izađite van");
                                    } else if (button2.getText().equals("Izađite van")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Izgleda da je jedina nada da izađete iz labirinta da uđete na put 'NE ULAZI'</div></html>");
                                        button1.setText("Ući na znak 'NE ULAZI'");
                                        button2.setText("Uzeli ste već blago");
                                        
                                 
                                        
                                    } else if (button2.getText().equals("Izađite na poljanu")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Boss Fight");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Borite se protiv čudovišta od trnja");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("Thornroot, Forest Spirit");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Health: 15");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Napad");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {15};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Health: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Porazili čudovište od trnja i labirint je nestao.");
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                } 
                                } 
                            });
                            
                            button1.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (button1.getText().equals("Ući na znak 'NE ULAZI'")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Dosta vremena hodate ravno i mislite da nema izlaza, nakon dužeg kretanja kroz koridor vidit kako put vodi na veliku poljanu ograđenu trnjem? Što će te dalje?</div></html>");
                                        button1.setText("Kreni nazad");
                                        button2.setText("Izađite na poljanu");
                                    } else if (button1.getText().equals("Kreni nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Izgleda da je jedina nada da izađete iz labirinta da uđete na put 'NE ULAZI'</div></html>");
                                    	button1.setText("Ući na znak 'NE ULAZI'");
                                        button2.setText("Ući na znak 'VELIKO BOGATSVO'");
                                    } else if (button1.getText().equals("Povratak nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Ušli ste zapušten svijet pun vegetacije. Ispred vas su 2 puta jedan vodi do močvare a drugi vodi do velikog labirinta trnja</div></html>");
                                        button1.setText("Prema močvari");
                                        button2.setText("Prema labirintu trnja");
                                    	
                                    } else if (button1.getText().equals("Izađite van")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Izgleda da je jedina nada da izađete iz labirinta da uđete na put 'NE ULAZI'</div></html>");
                                        button1.setText("Ući na znak 'NE ULAZI'");
                                        button2.setText("Uzeli ste već blago");
                                        
                                        //močvara
                                    } else if (button1.getText().equals("Prema močvari")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Močvara je zlokobna i maglovita, s trulim biljkama, muljevitim stazama i tamnim vodama koje kriju smrtonosne mutantne čudovišta.</div></html>");
                                        button1.setText("Vrati se nazad");
                                        button2.setText("Ući dublje u močvaru");
                                    } else if (button1.getText().equals("Vrati se nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Ušli ste zapušten svijet pun vegetacije. Ispred vas su 2 puta jedan vodi do močvare a drugi vodi do velikog labirinta trnja</div></html>");
                                        button1.setText("Prema močvari");
                                        button2.setText("Prema labirintu trnja");
                                    } else if (button1.getText().equals("Nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Močvara je zlokobna i maglovita, s trulim biljkama, muljevitim stazama i tamnim vodama koje kriju smrtonosne mutantne čudovišta.</div></html>");
                                        button1.setText("Vrati se nazad");
                                        button2.setText("Ući dublje u močvaru");
                                    } else if (button1.getText().equals("Idite nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Ušli ste dublje u močvaru i počnete halucinirat. Ispred vas vidite čovjeka koji se čini prijateljski, izgleda da vas zove da dođete.</div></html>");
                                        button1.setText("Nazad");
                                        button2.setText("Ignoriraj čovjeka i nastavi dalje");
                                    }else if (button1.getText().equals("U napad")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Boss Fight");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Borite se protiv čudovišta od blata");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("Swamphon, Mud golem");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Health: 30");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Napad");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {30};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Health: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Porazili čudovište od blata i izašli iz močvare.");
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                        
                                        
                                        
                                }
                                }
                            });
                                    	
                          
                            worldFrame.setVisible(true);
                        
                            //SVIJET TEHNOLOŠKOG KOLAPSA
                        }else if (selectedWorld.equals("Svijet Tehnološkog Kolapsa")) {
                        	JFrame worldFrame = new JFrame(selectedWorld);
                            worldFrame.setSize(600, 400); // Povećavamo veličinu prozora za prikaz svijeta
                            worldFrame.getContentPane().setLayout(new BorderLayout());

                            JLabel worldTitleLabel = new JLabel(selectedWorld);
                            worldTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            worldTitleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // Povećavamo veličinu fonta
                            worldFrame.getContentPane().add(worldTitleLabel, BorderLayout.NORTH);

                            // Dodajemo tekst iznad gumba
                            JLabel customTextLabel = new JLabel("<html><div style='text-align: center;'>Nalazite se u svijetu tehnološkog kolapsa. Lijevo su ruševine visokih tornjeva, a desno napušteni podzemni tuneli i opasni robotski stražari.)</div></html>");
                            customTextLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centriranje teksta
                            worldFrame.getContentPane().add(customTextLabel, BorderLayout.CENTER);

                            // Dodajemo panele za gumbe
                            JPanel buttonPanel = new JPanel(new GridLayout(3, 1)); // 3 reda, 1 stupac
                            JPanel buttonPanelRow1 = new JPanel(); // Dodani panel za poravnanje buttona
                            JPanel buttonPanelRow2 = new JPanel(); // Dodani panel za poravnanje buttona

                            JButton button1 = new JButton("Prema ruševinama tornjeva"); // Dodajemo tekst za prvi button
                            JButton button2 = new JButton("Prema mračnom tunelu"); // Dodajemo tekst za drugi button

                            // Dodajemo prazan prostor između prvog i drugog buttona (još veći)
                            buttonPanelRow1.add(button1);
                            buttonPanelRow1.add(Box.createHorizontalStrut(100)); // Dodajemo prazan prostor između gumba
                            buttonPanelRow1.add(button2);

                            // Dodajemo prazan prostor ispod prvog i drugog reda gumba
                            buttonPanelRow2.add(Box.createVerticalStrut(40)); // Dodajemo prazan prostor

                            buttonPanel.add(buttonPanelRow1);
                            buttonPanel.add(buttonPanelRow2); // Dodajemo panel s trećim buttonom ispod prvog i drugog
                            worldFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

                            

                            // Dodajemo ActionListener za button "Približiti se Vulkanu"/"Nastaviti istraživati vulkan"
                            button2.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (button2.getText().equals("Prema mračnom tunelu")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Ispred ulaza u tunel su 2 robota stražara. Što će te učiniti?</div></html>");
                                        button1.setText("Vratiti se nazad");
                                        button2.setText("Odvuči im pažnju kamenom");
                                    } else if (button2.getText().equals("Odvuči im pažnju kamenom")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Uspješno ste im odvukli pažnju.</div></html>");
                                        button1.setText("Vrati se nazad");
                                        button2.setText("Ušuljaj se u tunel");
                                    } else if (button2.getText().equals("Ušuljaj se u tunel")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Uspješno ste infiltrirali neprijateljevo sklonište.</div></html>");
                                        button1.setText("Kreni nazad");
                                        button2.setText("Nastavi istraživati tunel");
                                        
                                    } else if (button2.getText().equals("Nastavi istraživati tunel")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Nailzite na zaključana vrata morate pronaći stražara sa karticom za proći</div></html>");
                                        button1.setText("Odi nazad");
                                        button2.setText("Potraži stražara");
                                        
                                    } else if (button2.getText().equals("Potraži stražara")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Našli ste stražara, što dalje?.</div></html>");
                                        button1.setText("Kreni unazad");
                                        button2.setText("Ukradite karticu");
                                        
                                    } else if (button2.getText().equals("Ukradite karticu")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Vratite se i otključajte vrata</div></html>");
                                        button1.setText("Povratak");
                                        button2.setText("Otključaj vrata");
                                        
                                    } else if (button2.getText().equals("Otključaj vrata")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Ulazite u sobu i skužite da se tu nalaze tajne arhive. Što dalje?</div></html>");
                                        button1.setText("Povratak iza");
                                        button2.setText("Prebaciti arhive na usb i pobjeći");
                                        
                                    } else if (button2.getText().equals("Prebaciti arhive na usb i pobjeći")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Stavili ste arhive na usb ali vas napadne veliki robot zaštitar. Morate ga pobijediti da izađete</div></html>");
                                        button1.setText("Napadni robota");
                                        button2.setText("Napadni robota");
                                        
                                    }else if (button2.getText().equals("Napadni robota")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Boss Fight");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Borite se protiv robota žaštitara");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("R0B05, Guradian of Archives");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Health: 15");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Napad");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {15};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Health: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                	

                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Porazili ste robota i izašli iz tunela.");
                                                    
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                 
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                        
                                        //TORNJEVI BUTTON2
                                    } else if (button2.getText().equals("U potragu za tornjem")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Vidite u daljini toranj no između vas i tornja je desetak robota stražara.</div></html>");
                                        button1.setText("Nazad prema početku");
                                        button2.setText("Tiho se kreći iza ruševina tornjeva");
                                    } else if (button2.getText().equals("Tiho se kreći iza ruševina tornjeva")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Ispred tornja ste ali vas je jedan robot uočio</div></html>");
                                        button1.setText("Kreni vani dometa robota");
                                        button2.setText("Sakri se iza stijene pored");
                                    } else if (button2.getText().equals("Sakri se iza stijene pored")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Uspjeli ste se sakriti prije nego potvrdi jeste li prijetnja ili ne</div></html>");
                                        button1.setText("Okreni se");
                                        button2.setText("Popni se na toranj");
                                    } else if (button2.getText().equals("Popni se na toranj")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Postavljanje bomba");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Postavite 3 bombe na toranj");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("Glavni toranj");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Bombe: 3");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Postavi bombu");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {3};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Bombe: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Raznjeli ste toranj i onemogućili sve robote.");
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                        
                                        //TORNJEVI BUTTON2
                                    } 
                                }
                            });
                            
                            button1.addActionListener(new ActionListener() {
                                @Override
                                //button1 od tunela
                                public void actionPerformed(ActionEvent e) {
                                    if (button1.getText().equals("Vratiti se nazad")) {
                                        customTextLabel.setText("<html><div style='text-align: center;'>Nalazite se u svijetu tehnološkog kolapsa. Lijevo su ruševine visokih tornjeva, a desno napušteni podzemni tuneli i opasni robotski stražari.</div></html>");
                                        button1.setText("Prema ruševinama tornjeva");
                                        button2.setText("Prema mračnom tunelu");
                                    } else if (button1.getText().equals("Vrati se nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Ispred ulaza u tunel su 2 robota stražara. Što će te učiniti?</div></html>");
                                        button1.setText("Vratiti se nazad");
                                        button2.setText("Odvuči im pažnju kamenom");
                                    } else if (button1.getText().equals("Kreni nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Uspješno ste im odvukli pažnju.</div></html>");
                                        button1.setText("Vrati se nazad");
                                        button2.setText("Ušuljaj se u tunel");
                                    	
                                    } else if (button1.getText().equals("Odi nazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Uspješno ste infiltrirali neprijateljevo sklonište.</div></html>");
                                        button1.setText("Kreni nazad");
                                        button2.setText("Nastavi istraživati tunel");
                                    	
                                    } else if (button1.getText().equals("Vrati se unazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Nailzite na zaključana vrata morate pronaći stražara sa karticom za proći</div></html>");
                                        button1.setText("Odi nazad");
                                        button2.setText("Potraži stražara");
                                    	
                                    } else if (button1.getText().equals("Kreni unazad")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Nailzite na zaključana vrata morate pronaći stražara sa karticom za proći</div></html>");
                                        button1.setText("Odi nazad");
                                        button2.setText("Potraži stražara");
                                    	
                                    } else if (button1.getText().equals("Povratak")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Našli ste stražara, što dalje?.</div></html>");
                                        button1.setText("Kreni unazad");
                                        button2.setText("Ukradite karticu");
                                    	
                                    } else if (button1.getText().equals("Povratak iza")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Vratite se i otključajte vrata</div></html>");
                                        button1.setText("Povratak");
                                        button2.setText("Otključaj vrata");
                                    	
                                    } else if (button1.getText().equals("Izaći iz sobe")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Vratite se i otključajte vrata</div></html>");
                                        button1.setText("Povratak");
                                        button2.setText("Otključaj vrata");
                                    	
                                    } else if (button2.getText().equals("Napadni robota")) {
                                    	// Kreiramo novi prozor za boss fight
                                        JFrame bossFightFrame = new JFrame("Boss Fight");
                                        bossFightFrame.setSize(600, 400);
                                        bossFightFrame.getContentPane().setLayout(new BorderLayout());

                                        JLabel bossFightLabel = new JLabel("Borite se protiv robota žaštitara");
                                        bossFightLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossFightLabel.setFont(new Font("Arial", Font.BOLD, 30));
                                        bossFightFrame.getContentPane().add(bossFightLabel, BorderLayout.NORTH);

                                        // Panel za boss ime i health
                                        JPanel bossInfoPanel = new JPanel(new GridLayout(2, 1));

                                        JLabel bossNameLabel = new JLabel("R0B05, Guradian of Archives");
                                        bossNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossNameLabel);

                                        JLabel bossHealthLabel = new JLabel("Health: 15");
                                        bossHealthLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                        bossHealthLabel.setFont(new Font("Arial", Font.BOLD, 20));
                                        bossInfoPanel.add(bossHealthLabel);

                                        bossFightFrame.getContentPane().add(bossInfoPanel, BorderLayout.CENTER);

                                        // Panel za napad button
                                        JPanel attackPanel = new JPanel();
                                        JButton attackButton = new JButton("Napad");
                                        attackPanel.add(attackButton);
                                        bossFightFrame.getContentPane().add(attackPanel, BorderLayout.SOUTH);

                                        // Promjenjiva za praćenje healtha bossa
                                        final int[] bossHealth = {15};

                                        // Dodajemo ActionListener za attack button
                                        attackButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                bossHealth[0]--;
                                                bossHealthLabel.setText("Health: " + bossHealth[0]);
                                                if (bossHealth[0] == 0) {
                                                    JOptionPane.showMessageDialog(bossFightFrame, "Pobjeda! Porazili ste robota i izašli iz tunela.");
                                                    String feedback = JOptionPane.showInputDialog(bossFightFrame, "Kakav je vaš dojam igre?.(neobavezno):");

                                                    // Sprema povratne informacije u bazu podataka
                                                    saveFeedbackToDatabase(feedback);
                                                    bossFightFrame.dispose(); // Zatvara prozor boss fighta
                                                    worldFrame.dispose(); // Zatvara originalni prozor igre
                                                }
                                            }
                                        });

                                        bossFightFrame.setVisible(true);
                                        
                                    } else if (button1.getText().equals("Prema ruševinama tornjeva")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Nalazite se ispred ruševinama tornjeva. Morate naći jedan funkcionalan toranj sa kojeg se odašilje signal za robote i uništit ga.</div></html>");
                                        button1.setText("Nazad na početak");
                                        button2.setText("U potragu za tornjem");
                                    	
                                    } else if (button1.getText().equals("Nazad na početak")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Nalazite se u svijetu tehnološkog kolapsa. Lijevo su ruševine visokih tornjeva, a desno napušteni podzemni tuneli i opasni robotski stražari.</div></html>");
                                        button1.setText("Prema ruševinama tornjeva");
                                        button2.setText("Prema mračnom tunelu");
                                    	
                                    } else if (button1.getText().equals("Nazad prema početku")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Nalazite se ispred ruševinama tornjeva. Morate naći jedan funkcionalan toranj sa kojeg se odašilje signal za robote.</div></html>");
                                        button1.setText("Nazad na početak");
                                        button2.setText("U potragu za tornjem");
                                    	
                                    } else if (button1.getText().equals("Kreni vani dometa robota")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Vidite u daljini toranj no između vas i tornja je desetak robota stražara.</div></html>");
                                        button1.setText("Nazad prema početku");
                                        button2.setText("Tiho se kreći iza ruševina tornjeva");
                                    	
                                    } else if (button1.getText().equals("Okreni se")) {
                                    	customTextLabel.setText("<html><div style='text-align: center;'>Ispred tornja ste ali vas je jedan robot uočio</div></html>");
                                        button1.setText("Kreni vani dometa robota");
                                        button2.setText("Sakri se iza stijene pored");
                                    	
                                    }
                                }
                            });
                                    	
                          
                            worldFrame.setVisible(true);
                        
                        }
                    }
                });

                centerPanel.add(worldButton); // Dodajemo gumb na panel
            }
            connection.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
        gameFrame.getContentPane().add(centerPanel, BorderLayout.CENTER);

        gameFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}