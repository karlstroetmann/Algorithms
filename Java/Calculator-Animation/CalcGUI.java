
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;


/**
   A frame with a calculator panel.
*/
class CalcGUI extends JFrame
{
	private JFrame CalcFrame;
	private CalculatorPanel panel;
	
   public CalcGUI() 
   {
	  CalcFrame = new JFrame();
	  	  
	  CalcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      CalcFrame.setTitle("Calculator");
      panel = new CalculatorPanel();
      CalcFrame.add(panel);
      CalcFrame.pack();
      CalcFrame.setVisible(true);
   }
   
   public boolean run() {
	   if(panel.run) {
		   CalcFrame.setVisible(false);
	   }
	   return panel.run;
   }
   
   public ArrayList<Object> getTokenList() {
	   return panel.getTokenList();
   }
}

/**
   A panel with calculator buttons and a result display. 
*/
class CalculatorPanel extends JPanel
{
   public CalculatorPanel()
   {
	  run = false;
      setLayout(new BorderLayout());
      tokenList = new ArrayList<Object>();
      strPos = 0;


      result = 0;
      lastCommand = "=";
      start = true;

      // add the display

      display = new JButton("0");
      display.setEnabled(false);
      add(display, BorderLayout.NORTH);

      ActionListener insert = new InsertAction();
      ActionListener command = new CommandAction(); 

      // add the buttons in a 4 x 4 grid

      panel = new JPanel();
      panel.setLayout(new GridLayout(4, 5));

      addButton("7", insert);
      addButton("8", insert); 
      addButton("9", insert);
      addButton("/", insert);
      addButton("(", insert);
      addButton("sin", insert);

      addButton("4", insert);
      addButton("5", insert);
      addButton("6", insert);
      addButton("*", insert);
      addButton(")", insert);
      addButton("cos", insert);

      addButton("1", insert);
      addButton("2", insert);
      addButton("3", insert);
      addButton("-", insert);
      addButton("**", insert);
      addButton("sqrt", insert);

      addButton("RUN",command);
      addButton("0", insert);
      addButton(".", insert);
      addButton("+", insert);
      addButton("<-", command);
     addButton("STEP", command);
      

      add(panel, BorderLayout.CENTER);
   }

   /**
      Adds a button to the center panel.
      @param label the button label
      @param listener the button listener
   */
   private void addButton(String label, ActionListener listener) 
   {
      JButton button = new JButton(label);
      button.addActionListener(listener);
      panel.add(button);
   }

   /**
      This action inserts the button action string to the
      end of the display text. 
   */
   private class InsertAction implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         String input = event.getActionCommand();
         if (start)
         {
            display.setText("");
            start = false;
         }
         display.setText(display.getText() + input);
         
         //fill tokenList
         if( input == "." ){
        	 // do nothing more
         }else
         if( !Character.isDigit(input.charAt(0)) ) {
        	 
        	 //String parsing
        	 String str = new String();
        	 str = display.getText();
        	 str = str.substring(strPos,str.length()-input.length());
        	 strPos = display.getText().length();
        	 if(str.length() > 0 && 
        			 Character.isDigit(str.charAt(0))){
        	 tokenList.add(Double.parseDouble(str));
        	 tokenList.add( input );
        	 }
        	 else{
        		 tokenList.add( input );
        	 }
         }         
      }
   }

   /**
      This action executes the command that the button 
      action string denotes.
   */
   private class CommandAction implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         String command = event.getActionCommand ();
         
         if(command == "<-") {
        	 display.setText("0");
        	 tokenList.clear();
        	 start = true;
        	 strPos = 0;
         }
         
         if(command == "RUN" || command == "STEP") {
        	 //String parsing
        	 String str = new String();
        	 str = display.getText();
        	 str = str.substring(strPos,str.length());
        	 strPos = display.getText().length();
        	 if(Character.isDigit(display.getText().charAt(strPos - 1 ) ) ) {
        	 tokenList.add(Double.parseDouble(str));
        	 }
        	 
        	run = true;
        		 
        	 if(command == "STEP")
        		 Calculator.setStepMode(true);
        	 else
        		 Calculator.setStepMode(false);
        	 
        	 display.setText("0");
        	 start = true;
        	 strPos = 0;
        	 
         }
         
        
   	  }
   }

   /**
      Carries out the pending calculation.
      @param x the value to be accumulated with the prior result.
   */
   public void calculate(double x)
   {
      if ( lastCommand.equals("+")) result += x;
      else if (lastCommand.equals("-")) result -= x;
      else if (lastCommand.equals("*")) result *= x;
      else if (lastCommand.equals("/")) result /= x; 
      else if (lastCommand.equals("=")) result = x;
      else if (lastCommand.equals("<-")) result = x;
      display.setText("" + result);
   }
   
   public ArrayList<Object> getTokenList() {
	   return tokenList;
   }

   private JButton display;
   private JPanel panel;
   private double result;
   private String lastCommand; 
   private boolean start;
   private ArrayList<Object> tokenList;
   private int strPos;
   public boolean run;
}
