import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * Esta clase crea una cuenta atrás con una progressBar en otro hilo de ejecución de este modo no se bloquea
 * y el usuario puede cancelarlo cuando desee.
 * El usuario introduce el número de segundos de la cuenta atrás, que será mostrado en la barra de progreso y en
 * una etiqueta. Podrá iniciar o cancelar el proceso cuando desee.
 {@code @author:} Abraham Martínez
 {@code @version:} 28/11/2022
 * Asignatura: Programación Concurrente - UAX
 */

public class ProgressBar1 {
    boolean finalizar = false;
    boolean hilo_iniciado = false;
    JFrame mainFrame;
    JButton btnInicio;
    JButton btnCancelar;
    JProgressBar barraProgreso;
    Task task;
    Integer numSegundos;
    JTextField txtSegundos;
    JLabel lblSegundos;
    JLabel lblSegundosSal;
    String txtMensaje = "FIN Proceso";
    public ProgressBar1() { //constructor de la clase

        mainFrame = new JFrame("Cuenta atrás y barra de progreso");
        mainFrame.setSize(600, 400);
        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        txtSegundos = new JTextField();
        txtSegundos.setBounds(200, 40, 50, 30);

        lblSegundos = new JLabel("Número de Segundos: ");
        lblSegundos.setBounds(50, 40, 200, 30);
        lblSegundosSal = new JLabel("");
        lblSegundosSal.setBounds(400, 60, 200, 50);
        lblSegundosSal.setFont(new java.awt.Font("Tahoma", Font.BOLD, 50));
        //lblSegundosSal.setForeground(new java.awt.Color(255, 255, 255));

        barraProgreso = new JProgressBar(0, 10);
        barraProgreso.setBounds(50, 200, 500, 50);

        btnInicio = new JButton("Iniciar Hilo");
        btnInicio.setBounds(50, 100, 100, 50);
        btnCancelar = new JButton("Cancelar Hilo");
        btnCancelar.setBounds(200, 100, 120, 50);

        //creamos el listener para el boton inicio
        btnInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //cuando pulsamos inicio arrancamos
                try{
                    numSegundos = Integer.parseInt(txtSegundos.getText());
                    barraProgreso.setMaximum(numSegundos);
                    task = new Task();
                    task.start(); //iniciamos el hilo que gestiona la progressbar
                    txtMensaje = "FIN Proceso";
                }
                catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Debes introducir un número entero");
                    inicializarObjetos();
                }
            }
        });
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hilo_iniciado){
                    txtMensaje = "Cancelado por Usuario";
                    inicializarObjetos();
                    finalizar = true; //variable que indica al hilo paralelo que debe finalizar (bucle while)
                }
                else {
                    JOptionPane.showMessageDialog(null, "El Hilo no ha sido iniciado");
                }
            }
        });
        mainFrame.add(barraProgreso);
        mainFrame.add(btnInicio);
        mainFrame.add(btnCancelar);
        mainFrame.add(txtSegundos);
        mainFrame.add(lblSegundos);
        mainFrame.add(lblSegundosSal);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {//metodo main
        new ProgressBar1(); //instanciamos la clase, pintamos el formulario y sus objetos
    }

    void inicializarObjetos(){
        barraProgreso.setValue(0);
        txtSegundos.setText("");
        lblSegundosSal.setText("");
    }

    private class Task extends Thread { //clase embebida (inner class) que hereda de Thread y gestionará la progressbar
        Integer cont = numSegundos;
        @Override
        public void run() {  //sobreescribimos el método de la clase padre THREAD
            hilo_iniciado = true;
            while (!finalizar) { //mientras no se pulse boton Fin o fin ejecucion cuenta atras
                for (int i = 1; i <= numSegundos && !finalizar; i++) {
                    //repetimos el for durante los segundos indicados por usuario
                    barraProgreso.setValue(i);
                    lblSegundosSal.setText(Integer.toString(cont));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i==numSegundos){
                        lblSegundosSal.setText(Integer.toString(0));}
                    cont = cont -1; //contador para ir mostrando en lbl cta atrás de segundos
                }

                JOptionPane.showMessageDialog(null, txtMensaje);
                inicializarObjetos();
                finalizar= true; //Fin con éxito de la ejecución
            }
            hilo_iniciado= false; //bool para controlar si accion Cancelar tiene sentido
            finalizar = false; //estado inicial
        }
    }
}
