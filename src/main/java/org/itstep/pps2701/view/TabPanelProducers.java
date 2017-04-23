package org.itstep.pps2701.view;


import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dto.ProducerWrapper;
import org.itstep.pps2701.service.ProducerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
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
        producersTable = new JTable(tableModelBuider(producerService.findAllActive()));
        producersTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Редактировать выбранную запись");

        addBtn.addActionListener(b -> createAddDialog());
        editBtn.addActionListener(b -> createEditDialog(producersTable.getSelectedRow()));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);

        tabPanelProducers = new JPanel(new BorderLayout(5,5));
        tabPanelProducers.add(new JScrollPane(producersTable), BorderLayout.CENTER);
        tabPanelProducers.add(btnPanel, BorderLayout.SOUTH);
    }

    // создание диалога для добавления записи в бд
    private void createAddDialog() {
        JDialog addDialog = new JDialog(parentFrame, "Добавление производителя", true);
        addDialog.setLocationRelativeTo(parentFrame);

        JTextField txtFieldName = new JTextField(25);
        JTextField txtFieldCountry = new JTextField(25);

        txtFieldName.setToolTipText(lblProducerName.getText());
        txtFieldCountry.setToolTipText(lblProducerCountry.getText());

        JButton saveBtn = new JButton("Сохранить");
        JButton cancelBtn = new JButton("Отмена");

        saveBtn.addActionListener( b -> {
            try{
                ProducerWrapper producerWrapper = new ProducerWrapper();
                // если поля логина и пароля не пустые
                if(!"".equals(txtFieldCountry.getText()) && !"".equals(txtFieldName.getText())){
                    producerWrapper.setDateOpen(new Timestamp(System.currentTimeMillis()));
                    producerWrapper.setName(txtFieldName.getText());
                    producerWrapper.setCountry(txtFieldCountry.getText());

                    java.util.List<ProducerWrapper> producerWrapperList = producerService.create(producerWrapper);

                    producersTable.setModel(tableModelBuider(producerWrapperList));
                    addDialog.dispose();
                } else {
                    parentFrame.createErrorDialog("Проверьте правильноcть ввода данных");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        cancelBtn.addActionListener(b -> addDialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        JPanel insertDialogPanel = new JPanel(new GridLayout(5,1));
        insertDialogPanel.add(lblProducerName);
        insertDialogPanel.add(txtFieldName);
        insertDialogPanel.add(lblProducerCountry);
        insertDialogPanel.add(txtFieldCountry);

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

        String id = String.valueOf(producersTable.getValueAt(selectedRow, 0));

        ProducerWrapper producerWrapper = new ProducerWrapper();
        producerWrapper.setId(id);
        try{
            producerWrapper = producerService.read(id);
        } catch (Exception ex){
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        JTextField txtFieldName = new JTextField(25);
        JTextField txtFieldCountry = new JTextField(25);

        txtFieldName.setText(producerWrapper.getName());
        txtFieldCountry.setText(producerWrapper.getCountry());

        txtFieldName.setToolTipText(lblProducerName.getText());
        txtFieldCountry.setToolTipText(lblProducerCountry.getText());

        JButton updateBtn = new JButton("Сохранить");
        JButton removeBtn = new JButton("Удалить");
        JButton cancelBtn = new JButton("Отмена");

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
                    producersTable.setModel(tableModelBuider(producerWrapperList));
                    editDialog.dispose();
                }catch (Exception ex){
                    ex.printStackTrace();
                    parentFrame.createErrorDialog(ex.getMessage());
                }
            }
        });

        removeBtn.addActionListener(b -> {
            try{
                java.util.List<ProducerWrapper> producerWrapperList = producerService.delete(id);
                producersTable.setModel(tableModelBuider(producerWrapperList));
                editDialog.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        cancelBtn.addActionListener(b -> editDialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.add(updateBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(cancelBtn);

        JPanel editDialogPanel = new JPanel(new GridLayout(7,1));
        editDialogPanel.add(lblProducerName);
        editDialogPanel.add(txtFieldName);
        editDialogPanel.add(lblProducerCountry);
        editDialogPanel.add(txtFieldCountry);
        editDialogPanel.add(btnPanel, "south");

        editDialog.add(editDialogPanel);
        editDialog.pack();
        editDialog.setVisible(true);
    }


    public DefaultTableModel tableModelBuider(java.util.List<ProducerWrapper> producerWrapperList) {
        String[] tableHeader = {
                "id",
                "Наименование производителя",
                "Страна производителя",
                "Дата создания",
                "Дата закрытия"
        };
        DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 0);

        for(ProducerWrapper producerWrapper : producerWrapperList) {
            tableModel.addRow(producerWrapper.toObject());
        }
        return tableModel;
    }
}
