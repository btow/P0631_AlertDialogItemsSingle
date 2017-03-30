package com.example.samsung.p0631_alertdialogitemssingle;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    final int DIALOG_ITEMS = 1,
            DIALOG_ADAPTER = 2,
            DIALOG_CURSOR = 3;
    String[] data;
    DB db;
    Cursor cursor;
    //Обработчик нажатия на пункт списка в диалоге или кнопку
    DialogInterface.OnClickListener myClicListener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            ListView listView = ((AlertDialog) dialog).getListView();
            String message = "";

            if (which == Dialog.BUTTON_POSITIVE) {
                //Формирование строки вывода в лог  и всплывающее сообщение позиции выбранного элемента
                message += (getString(R.string.position_number) + listView.getCheckedItemPosition());
            } else {
                //Формирование строки вывода в лог  и всплывающее сообщение позиции нажатого элемента
                message += (getString(R.string.rowse_number) + which);
            }
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = getResources().getStringArray(R.array.data);
        db = new DB(this);
        db.open();
        cursor = db.getAllData();
        startManagingCursor(cursor);

    }

    public void onClickButton(View view) {
        switch (view.getId()) {
            case R.id.btnItems:
                showDialog(DIALOG_ITEMS);
                break;
            case R.id.btnAdapter:
                showDialog(DIALOG_ADAPTER);
                break;
            case R.id.btnCursor:
                showDialog(DIALOG_CURSOR);
                break;
            default:
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        int checkedItem = -1;
        switch (id) {
            //Диалог из Массива
            case DIALOG_ITEMS:
                adb.setTitle(R.string.items);
                //Устанавливаем выбранный элемент
                adb.setSingleChoiceItems(data, checkedItem, myClicListener);
                break;
            //Диалог из Адаптера
            case DIALOG_ADAPTER:
                adb.setTitle(R.string.adapter);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this, android.R.layout.select_dialog_singlechoice, data
                );
                //Устанавливаем выбранный элемент
                adb.setSingleChoiceItems(adapter, checkedItem, myClicListener);
                break;
            //Диалог из Курсора
            case DIALOG_CURSOR:
                adb.setTitle(R.string.cursor);
                //Устанавливаем выбранный элемент
                adb.setSingleChoiceItems(cursor, checkedItem, DB.COLUMN_TXT, myClicListener);
                break;
        }
        adb.setPositiveButton(R.string.ok, myClicListener);
        return adb.create();
    }

    protected void onPrepareDialog(int id, Dialog dialog) {
        //Доступ к адаптеру списка диалога
        AlertDialog aDialog = (AlertDialog) dialog;
        int checkedItemPosition = aDialog.getListView().getCheckedItemPosition();
        //Сброс выбранных пунктов диалога
        aDialog.getListView().setItemChecked(checkedItemPosition, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
