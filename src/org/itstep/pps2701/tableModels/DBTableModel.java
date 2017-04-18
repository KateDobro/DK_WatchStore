//package org.itstep.pps2701.tableModels;
//
//import javax.swing.table.AbstractTableModel;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.util.ArrayList;
//
//// Модель данных таблицы, работающая с запросами к базам данных
//public class DBTableModel extends AbstractTableModel {
//    private ArrayList columnNames = new ArrayList(); // названия столбцов
//    private ArrayList columnTypes = new ArrayList(); // типы столбцов
//    private ArrayList data = new ArrayList();        // данные полученные из БД
//    private boolean editable;
//    // конструктор позволяет задать возможность редактирования
//    public DBTableModel(boolean editable) {
//        this.editable = editable;
//    }
//
//    @Override // кол-во строк таблицы БД
//    public int getRowCount() {
//        synchronized (data){
//            return data.size();
//        }
//    }
//
//    @Override // кол-во столбцов таблицы БД
//    public int getColumnCount() {
//        return columnNames.size();
//    }
//
//    // тип данных столбца
//    public Class getColumnClass(int column) {
//        return (Class)columnTypes.get(column);
//    }
//
//    // название столбца
//    public String getColumnName(int column) {
//        return (String)columnNames.get(column);
//    }
//
//    @Override  // данные в ячейке [row, column]
//    public Object getValueAt(int row, int column) {
//        synchronized (data){
//            return ((ArrayList)data.get(row)).get(column);
//        }
//    }
//
//    // возможность редактирования
//    public boolean isEditable(int row, int column){
//        return editable;
//    }
//
//    // замена значения ячейки row, column на value
//    public void setValueAt(Object value, int row, int column){
//        synchronized (data) {
//            ((ArrayList)data.get(row)).set(column, value);
//        }
//    }
//
//    // получение данных из объекта ResultSet
//    public void setDataSource(ResultSet resultSet) throws Exception{
//        //очистка прежних данных
//        data.clear();
//        columnNames.clear();
//        columnTypes.clear();
//
//        // получаем вспомогательную информацию о столбцах из метаданных
//        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
//        int columnCount = resultSetMetaData.getColumnCount();
//        for (int i=0; i < columnCount;i++){
//            // название столбца
//            columnNames.add(resultSetMetaData.getColumnName(i+1));
//            // тип данных столбца
//            Class type = Class.forName(resultSetMetaData.getColumnClassName(i+1));
//            columnTypes.add(type);
//        }
//
//        // сообщаем об изменениях в структуре данных
//        fireTableStructureChanged(); // звжигаем событие, извещаем всех слушателей
//
//        while(resultSet.next()){
//            // здесь будем хранить ячейки одной строки
//            ArrayList row = new ArrayList();
//            for(int i=0; i < columnCount;i++){
//                if(columnTypes.get(i) == String.class){
//                    row.add(resultSet.getString(i+1));
//                } else {
//                    row.add(resultSet.getObject(i+1));
//                }
//            }
//
//            synchronized (data){
//                data.add(row);
//
//                // сообщаем о прибавлении строки
//                fireTableRowsInserted(data.size() - 1, data.size() - 1);
//            }
//        }
//    }
//}
