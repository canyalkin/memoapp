package canyalkin.net.memoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AppUtil {
	
	public static void showAlertMessage(Context context, String msg) {
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
		
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle("Error Message...");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) 
            {   
               dialog.cancel();
            }
        });
        
        dlgAlert.create().show();
        
	}

}
