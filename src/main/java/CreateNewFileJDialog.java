import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateNewFileJDialog extends JDialog {
        private JTextField nameOfNewFolder = new JTextField(10);//Поле для ввода текста 10 символов
        private JButton okButton = new JButton("Создать");// Кнопка создать
        private JButton cancelButton = new JButton("Отмена");// Кнопка отмена
        private String newFolderName;
        private JLabel nameFolderWait = new JLabel("Имя нового файла: ");//Просто надпись
        private boolean ready = false;

        CreateNewFileJDialog(JFrame jFrame) {
            super(jFrame,"Создать новый файл", true);//нелязя работать с приложением пока не закончим с окном // Создание всплывающего окна 1)К чему относится, как будет называтся, можно ли закрыть
            setLayout(new GridLayout(2,2,5,5));// 2 строки 2 элемента
            setSize(400,200);//Стандартный размер
            okButton.addActionListener(new ActionListener() {//Отлавливаем нажатие
                @Override
                public void actionPerformed(ActionEvent e) {
                    newFolderName = nameOfNewFolder.getText();//Получить иля написаной папки
                    setVisible(false);//видимость окна после назатия,тк оно отработало
                    ready = true;// готовность к созданию
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);//ВИДИМОСТЬ ПОСЛЕ НАЖАТИЯ ОТМЕНЫ
                    ready = false;// ГОТОВНОСТЬ
                }
            });

            getContentPane().add(nameFolderWait);//Показать надпись
            getContentPane().add(nameOfNewFolder);// строчка для написания надписи
            getContentPane().add(okButton);// кнопка создать
            getContentPane().add(cancelButton);// кнопка отмена
            pack();
            setLocationRelativeTo(null);
            setVisible(true); //Видимоть всего
        }

        public boolean getReady(){ // метод возвращиет готовность
            return ready;
        }

        public String getNewName(){ // Метод возвращает новое имя
            return newFolderName;
        }

    }

