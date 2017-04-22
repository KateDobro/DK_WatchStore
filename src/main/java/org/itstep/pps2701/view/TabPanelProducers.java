package org.itstep.pps2701.view;


import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dto.ProducerWrapper;
import org.itstep.pps2701.service.ProducerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;

// Вкладка пользователей
public class TabPanelProducers extends JPanel{
    private static JLabel lblProducerName = new JLabel("Название производителя");
    private static JLabel lblProducerCountry = new JLabel("Страна производителя");

    private JTable producersTable;
    private JPanel tabPanelProducers;
    private MainFrame parentFrame;

    private ProducerService producerService; // действия производимые с пользователями

    public TabPanelProducers(JTabbedPane tabbedPane, MainFrame parentFrame) {
        producerService= Utils.getInjector().getInstance(ProducerService.class);

        this.parentFrame = parentFrame;
        buildTabPanelProducers();
        tabbedPane.addTab("Производители", tabPanelProducers);
    }

    private void buildTabPanelProducers() {
        tabPanelProducers = new JPanel(new BorderLayout(5,5));

        producersTable = new JTable(producersTableBuider(producerService.findAllActive()));

        producersTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabPanelProducers.add(new JScrollPane(producersTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // + панель управляющих кнопок вкладки "Пользователи"
        JButton addBtn = new JButton("Добавить");
        addBtn.addActionListener(b -> createAddDialog());
        btnPanel.add(addBtn);

        JButton editBtn = new JButton("Редактировать выбранную запись");
        editBtn.addActionListener(b -> createEditDialog(producersTable.getSelectedRow()));
        btnPanel.add(editBtn);

        tabPanelProducers.add(btnPanel, BorderLayout.SOUTH);
    }

    // создание диалога для добавления записи в бд
    private void createAddDialog() {
        JDialog addDialog = new JDialog(parentFrame, "Добавление производителя", true);
        addDialog.setLocationRelativeTo(parentFrame);

        JPanel insertDialogPanel = new JPanel();
        insertDialogPanel.setLayout(new GridLayout(5,1));

        insertDialogPanel.add(lblProducerName);
        JTextField txtFieldName = new JTextField(25);
        txtFieldName.setToolTipText(lblProducerName.getText());
        insertDialogPanel.add(txtFieldName);

        insertDialogPanel.add(lblProducerCountry);
        JTextField txtFieldCountry = new JTextField(25);
        txtFieldCountry.setToolTipText(lblProducerCountry.getText());
        insertDialogPanel.add(txtFieldCountry);

        JPanel btnPanel = new JPanel();

        JButton saveBtn = new JButton("Сохранить");
        saveBtn.addActionListener( b -> {
            try{
                ProducerWrapper producerWrapper = new ProducerWrapper();
                // если поля логина и пароля не пустые
                if(!"".equals(txtFieldCountry.getText()) && !"".equals(txtFieldName.getText())){
                    producerWrapper.setDateOpen(new Timestamp(System.currentTimeMillis()));
                    producerWrapper.setName(txtFieldName.getText());
                    producerWrapper.setCountry(txtFieldCountry.getText());

                    java.util.List<ProducerWrapper> producerWrapperList = producerService.create(producerWrapper);

                    producersTable.setModel(producersTableBuider(producerWrapperList));
                    addDialog.dispose();
                } else {
                    parentFrame.createErrorDialog("Проверьте правильноcть ввода данных");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(b -> addDialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        insertDialogPanel.add(btnPanel, "south");
        addDialog.add(insertDialogPanel);
        addDialog.pack();
        addDialog.setVisible(true);
    }

    /**
     * создание диалогового окна редактирования записи производителя
     * @param selectedRow номер выбранного ряда таблицы
     */
    private void createEditDialog(int selectedRow) {
        JDialog editDialog = new JDialog(parentFrame, "Редактирование данных о производителе", true);
        editDialog.setLocationRelativeTo(parentFrame);

        JPanel editDialogPanel = new JPanel(new GridLayout(7,1));

        int id = (Integer) producersTable.getValueAt(selectedRow, 0);

        ProducerWrapper producerWrapper = new ProducerWrapper();
        producerWrapper.setId(id);
        try{
            producerWrapper = producerService.getProducerById(id);
        } catch (Exception ex){
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        editDialogPanel.add(lblProducerName);
        JTextField txtFieldName = new JTextField(producerWrapper.getName(), 25);
        txtFieldName.setToolTipText(lblProducerName.getText());
        editDialogPanel.add(txtFieldName);

        editDialogPanel.add(lblProducerCountry);
        JTextField txtFieldCountry = new JTextField(producerWrapper.getCountry(), 25);
        txtFieldCountry.setToolTipText(lblProducerCountry.getText());
        editDialogPanel.add(txtFieldCountry);

        JPanel btnPanel = new JPanel();
        JButton updateBtn = new JButton("Сохранить");
        updateBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    ProducerWrapper producerWrapperFin = new ProducerWrapper();
                    if(!"".equals(txtFieldName.getText()) && !"".equals(txtFieldCountry.getText())) {
                        producerWrapperFin.setId(id);
                        producerWrapperFin.setName(txtFieldName.getText());
                        producerWrapperFin.setCountry(txtFieldCountry.getText());
                    } else {
                        parentFrame.createErrorDialog("Проверьте корректность ввода данных");
                    }
                    java.util.List<ProducerWrapper> producerWrapperList = producerService.update(producerWrapperFin); // вызов метода обновления данных пользователя + перестройка данных в таблице
                    producersTable.setModel(producersTableBuider(producerWrapperList));
                    editDialog.dispose();
                }catch (Exception ex){
                    ex.printStackTrace();
                    parentFrame.createErrorDialog(ex.getMessage());
                }
            }
        });

        JButton removeBtn = new JButton("Удалить");
        removeBtn.addActionListener(b -> {
            try{
                java.util.List<ProducerWrapper> producerWrapperList = producerService.remove(id);
                producersTable.setModel(producersTableBuider(producerWrapperList));
                editDialog.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(b -> editDialog.dispose());

        btnPanel.add(updateBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(cancelBtn);

        editDialogPanel.add(btnPanel, "south");
        editDialog.add(editDialogPanel);
        editDialog.pack();
        editDialog.setVisible(true);
    }


    public DefaultTableModel producersTableBuider(java.util.List<ProducerWrapper> producerWrapperList) {
        String[] tableHeader = {"id", "Дата создания записи", "Дата закрытия записи", "Название производителя", "Страна производителя"};
        DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 0);

        for(ProducerWrapper producerWrapper : producerWrapperList) {
            tableModel.addRow(producerWrapper.toObject());
        }
        return tableModel;
    }
}
