package net.jgl2d.sys;

import net.jgl2d.behaviour.Behaviour;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.Arrays;


/**
 * Created by peter on 7/19/15.
 */
public class BehaviourInspector {
    private Behaviour behaviour;
    private JFrame windowFrame;

    public BehaviourInspector(final Behaviour behaviour) {
        this.behaviour = behaviour;
        windowFrame = new JFrame("Inspector - " + behaviour.getClass().getSimpleName());
        windowFrame.setSize(200, 240);
        windowFrame.add(new JLabel(behaviour.getClass().getSimpleName()));
        windowFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                windowFrame.setVisible(false);
            }
        });
        windowFrame.getContentPane().setLayout(null);

        int i = 0;
        for(Field field : behaviour.getClass().getFields()) {
            try {
                field.setAccessible(true);
                boolean editable = Arrays.asList(float.class, int.class, String.class).contains(field.getType());

                JLabel label = new JLabel(field.getName() + (editable ? ":" : ": - "));
                label.setBounds(0, i * 20, 100, 20);

                windowFrame.getContentPane().add(label);
                if(editable) {
                    final JTextField textfield = new JTextField(field.get(behaviour).toString());
                    final Field refersTo = field;
                    textfield.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            String raw = textfield.getText();
                            if(refersTo.getType() == float.class) {
                                try {
                                    refersTo.set(behaviour, Float.parseFloat(raw));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            } else if(refersTo.getType() == int.class) {
                                try {
                                    refersTo.set(behaviour, Integer.parseInt(raw));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    refersTo.set(behaviour, raw);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    windowFrame.add(textfield).setBounds(100, i * 20, 100, 20);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            i++;
        }
        windowFrame.setVisible(true);
    }
}
