/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Matrix
 */
public class formEditarTermo {

    private view.ModelTermos TableModel = null;
    private view.formEditarTermos tela = null;
    private ArrayList<String> ListaTermos = null;
    private sqlite.Conection banco = null;
    private String termoAtual = "";

    public formEditarTermo() throws IOException {
        tela = new view.formEditarTermos(null, true);
        ListaTermos = new ArrayList<String>();
        TableModel = new view.ModelTermos();
        tela.tableTermos.setModel(TableModel);
        banco = new sqlite.Conection();

        ligaEventos();
    }

    private void mouseCliqueTable() {

        int Posicao = tela.tableTermos.getSelectedRow();
        String termo = TableModel.GetPosition(Posicao);
        termoAtual = termo;
        tela.edTermo.setText(termo);

    }

    private void sair() {
        tela.setVisible(false);
    }

    private void alterar() throws IOException {
        String novoTermo = tela.edTermo.getText();
        if (novoTermo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Obrigatório informar o novo termo para alterar.");
        } else if (!novoTermo.equalsIgnoreCase(termoAtual)) {
            banco.setUpdateTermo(termoAtual, novoTermo);
            CarregaTableModel();
            JOptionPane.showMessageDialog(null, "Alterado com sucesso!");
            limparTela();
        }
    }

    private void ligaEventos() throws IOException {

        tela.btnAlterar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    alterar();
                } catch (IOException ex) {
                    Logger.getLogger(formEditarSites.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        tela.btnSair.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sair();
            }
        });

        tela.tableTermos.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                mouseCliqueTable();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }

    private void limparTela() {
        tela.edTermo.setText("");
    }

    public void chamaTelaEditarTermos() throws IOException {
        limparTela();
        CarregaTableModel();
        tela.setVisible(true);
    }

    private void CarregaTableModel() throws FileNotFoundException, IOException {
        ListaTermos.clear();
        TableModel.Limpar();
        ListaTermos = banco.getTermos();

        for (int i = 0; i < ListaTermos.size(); i++) {
            TableModel.AddTermo(ListaTermos.get(i));
        }

    }

}
