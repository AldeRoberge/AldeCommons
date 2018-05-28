# AldeCommons

AldeCommons is a compilation of many utilities used by a multitude of my own personal projects.

As it evolves as I learn, I do not claim it is exemplary.

I aim for it to be compact and very simple to use and understand.


























JButton button = new JButton("X");
button.setFocusable(false); //Removes the square 'border' when clicked
button.setToolTipText("Delete"); //Hover with mouse shows this text
button.setFont(new Font("Tahoma", Font.BOLD, 13));
button.setForeground(new Color(255, 0, 0)); //Changes the text color
button.setMargin(new Insets(-5, -5, -5, -5)); //allows for the text/icon to display even if theres not enough space (otherwise it will do something like th...)
    button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
	           //Perform something
        }
    });
button.setBounds(290, 7, 35, 35);
panel.add(button);


















JCheckBox chckbxNewCheckBox = new JCheckBox("Inclusif/Exclusif");
		
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (chckbxNewCheckBox.isSelected()) {
					
				} else {
					
				}
			}
		};
chckbxNewCheckBox.addActionListener(actionListener);



/**
* You might want to open a file in the explorer, selected (works cross platform)
*/

try {
	Desktop.getDesktop().open(file.getParentFile());
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}



try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
	    | UnsupportedLookAndFeelException e1) {
	    e1.printStackTrace();
}