package com.mta.sadna19.sadna;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mta.sadna19.sadna.MenuRegisters.DataOption;
import com.mta.sadna19.sadna.MenuRegisters.Menu;
import com.mta.sadna19.sadna.MenuRegisters.MenuFactory.Factory;
import com.mta.sadna19.sadna.MenuRegisters.Option;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button dialBtn;
    EditText phone;
    private DatabaseReference dataref;
    public static final String TAG = "onMain>>";
   /* //doing test to see update on FireBase
    FirebaseDatabase dataBase;
    DatabaseReference dataBaseRef;
    DataOption testOption ;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate() >>");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //String encodedHash = Uri.encode("#");
        //String str = "#";
        //Hot

        //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "*6900,%23"));
        //startActivity(intent);
        init();
        Log.e(TAG, "onCreate() <<");
    }

    private void init() {
        Log.e(TAG, "init() >>");
        phone = findViewById(R.id.phoneEditText);
        dialBtn = findViewById(R.id.dialButton);
        dataref = FirebaseDatabase.getInstance().getReference("Menus");

        Option bezeq = testFactory();
        if (bezeq == null) {
            Log.e(TAG, "bezeq is NULL");
        }
        Log.e(TAG, "onInit name is " + bezeq.getName());


        ServerHandler test = new ServerHandler();

        ServiceItem item = new ServiceItem();

/*
        //Metropolin
        item.setM_genre("תחבורה");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FMetropolin.png?alt=media&token=b754193b-657f-4edf-9da8-78f816099a8c");
        item.setM_name("מטרופולין");
        test.writeNewService(MetropolinFactory(), item);


        //Maccabi
        item.setM_genre("בריאות");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FMacabi.jpg?alt=media&token=f8299f1e-6546-44eb-8867-48e185af2fdd");
        item.setM_name("מכבי");
        test.writeNewService(MacabbiFactory(), item);


        //Pelephone
        item.setM_genre("סלולר");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FPelephone.png?alt=media&token=5897aa86-880c-4e6d-9ffd-4e415b233c6d");
        item.setM_name("פלאפון");
        test.writeNewService(PelephoneFactory(), item);

        //Meuhedet
        item.setM_genre("בריאות");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FMeuhedet.png?alt=media&token=22695947-7ada-4952-9329-862e1c639d04");
        item.setM_name("מאוחדת");
        test.writeNewService(MeuhedetFactory(), item);

        //MisradHaPnim
        item.setM_genre("ממשלתי");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FMisradHaPnim.jpg?alt=media&token=b2ead621-4cbc-4f0e-a867-8b05fc550937");
        item.setM_name("משרד הפנים");
        test.writeNewService(MisradHapnim(), item);

        //BituahLeumi
        item.setM_genre("ממשלתי");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FBituahLeumi.png?alt=media&token=6daa6628-5106-41bb-93e2-82f0b2ead235");
        item.setM_name("ביטוח לאומי");
        test.writeNewService(BituahLeumi(), item);
*/
        //MisradHaRishuy
        item.setM_genre("ממשלתי");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FMisradHaRishuy.jpg?alt=media&token=0ccbc371-49b0-4159-a888-b343b8719da7");
        item.setM_name("משרד הרישוי");
        test.writeNewService(MisradHarishuy(), item);
/*
        //IryatTA
        item.setM_genre("ממשלתי");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FIryatTelAviv.png?alt=media&token=2448b7d2-5e0f-495e-a3de-1e1f55ae5023");
        item.setM_name("עיריית תל אביב");
        test.writeNewService(IriyatTA(), item);


        //Hot
        item.setM_genre("תקשורת");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FHot.png?alt=media&token=5dcaf978-1b33-4b3c-8cce-31c2e97afed1");
        item.setM_name("הוט");
        test.writeNewService(HotFactory(), item);

        //Eged
        item.setM_genre("תחבורה");
        item.setM_avatar("https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FEged.png?alt=media&token=de75a3fe-2618-415f-a74b-0aa6d3c6352c");
        item.setM_name("אגד");
        test.writeNewService(EgedFactory(), item);

*/
        Log.e(TAG, "init() <<");
    }


    public Option testFactory() {
        Log.e(TAG, "testFactory() >>");

        Factory factory = new Factory();
        Log.e(TAG, "testFactory() <<");
        return factory.CreateOption("1800201229", "אוצר החייל")
                .AddSubMenu(
                        factory.CreateOption("1", "לקוח קיים").AddSubMenu(
                                factory.CreateDataOption("אנא הזני מספר טלפון", "PHONE", "%23").AddSubMenu(
                                        factory.CreateOption("1", "שירות לקוחות"),
                                        factory.CreateOption("2", "תמיכה טכנית")
                                )
                        ),
                        factory.CreateOption("2", "לקוח חדש")).GetOption();
    }


    public void buttonOnClick(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:1800201229,2," + phone.getText().toString() + ",2,,1"));


        startActivity(intent);
    }


    //=====================================================================================
//Eged


    public Option MetropolinFactory() {
        Log.e(TAG, "testFactory() >>");

        Factory factory = new Factory();
        Log.e(TAG, "testFactory() <<");
        return factory.CreateOption("*5900", "מטרופולין").ChangePostKeys(",%23,,")
                .AddSubMenu(

                        factory.CreateOption("1", "אזור נתניה וגוש דן")
                                .AddSubMenu(
                                        factory.CreateOption("1", "בירור בנושא אבידות באזור נתניה"),
                                        factory.CreateOption("2", "בירור בנושא אבידות בגוש דן חולון והסביבה"),
                                        factory.CreateOption("3", "לפניות הציבור או בכל נושא אחר")
                                ),
                        factory.CreateOption("2", "אזור השרון")
                                .AddSubMenu(
                                        factory.CreateOption("1", "לפנייה בנושא אבידות"),
                                        factory.CreateOption("2", "לפניות הציבור או בכל נושא אחר")
                                ),
                        factory.CreateOption("3", "אזור הדרום")
                                .AddSubMenu(
                                        factory.CreateOption("1", "לפנייה בנושא אבידות"),
                                        factory.CreateOption("2", "לפניות הציבור או בכל נושא אחר")
                                ),
                        factory.CreateOption("4", "למענה אודות הגעת האוטובוס הבא לתחנה או זמני נסיעות עתידיים").ChangePostKeys(",,,")
                                .AddSubMenu(
                                        factory.CreateOption("1", "לפי תחנה").
                                                AddSubMenu(
                                                        factory.CreateDataOption("נא הקש מספר תחנה", "", "%23").ChangePostKeys("%23,")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("נא הקש מספר קו", "", "%23")
                                                                )
                                                ),
                                        factory.CreateOption("2", "לפי קו")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("נא הקש מספר קו", "", "%23")
                                                ),
                                        factory.CreateOption("3", "לוחות זמנים")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("נא הקש מספר קו", "", "%23")
                                                )
                                ),
                        factory.CreateOption("5", "למחפשי עבודה"),
                        factory.CreateOption("6", "לפרסום על האוטובוסים"),
                        factory.CreateOption("7", "להסעות פרטיות")


                ).GetOption();
    }

    public Option MacabbiFactory() {
        Log.e(TAG, "testFactory() >>");

        Factory factory = new Factory();
        Log.e(TAG, "testFactory() <<");
        return factory.CreateOption("*3555,,,,,,1,,,,%23,", "מכבי")
                .AddSubMenu(
                        factory.CreateOption("*2", "הצטרפות למכבי שלי"),
                        factory.CreateOption("1", "זימון וביטול תורים").ChangePostKeys(",,").AddSubMenu(
                                factory.CreateOption("1", "לזימון וביטול תורים לרופאים").AddSubMenu(
                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                ),
                                factory.CreateOption("2", "לזימון וביטול תורים לבדיקות רנטגן,שיקוף ואולטרסאונד").AddSubMenu(
                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                ),
                                factory.CreateOption("3", "לזימון וביטול תורים למכונים,רופאים, פיזיותרפיה וטיפת חלב").AddSubMenu(
                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                ),
                                factory.CreateOption("4", "לזימון וביטול תורים למרפאת חיות").AddSubMenu(
                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                ),
                                factory.CreateOption("5", "לזימון וביטול תורים לבדיקות מעבדה").AddSubMenu(
                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                ),
                                factory.CreateOption("6", "לזימון וביטול תורים דיאטניות ועובדים סוציאליים").AddSubMenu(
                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                )
                        ),
                        factory.CreateOption("2", "מידע אודות כתובות,טלפונים ושעות פעילות של שירותי מכבי"),
                        factory.CreateOption("3", "מידע אודות זכויות,ביטוחים משלימים,תשלומים ומידע נוסף")
                                .AddSubMenu(
                                        factory.CreateOption("1", "לקבלת ססטוס הטיפול").AddSubMenu(
                                                factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")),
                                        factory.CreateOption("2", "נציג").AddSubMenu(
                                                factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23"))
                                ),
                        factory.CreateOption("*3", "תמיכה באתר מכבי אונליין"),
                        factory.CreateOption("4", "מוקדי ייעוץ רפואי").ChangePostKeys(",,,,,,,,")
                                .AddSubMenu(
                                        factory.CreateOption("1", "זימון תור למרפאת אחיות").AddSubMenu(

                                                factory.CreateOption("1", "לזימון וביטול תורים לרופאים").AddSubMenu(
                                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                                ),
                                                factory.CreateOption("2", "לזימון וביטול תורים לבדיקות רנטגן,שיקוף ואולטרסאונד").AddSubMenu(
                                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                                ),
                                                factory.CreateOption("3", "לזימון וביטול תורים למכונים,רופאים, פיזיותרפיה וטיפת חלב").AddSubMenu(
                                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                                ),
                                                factory.CreateOption("4", "לזימון וביטול תורים למרפאת חיות").AddSubMenu(
                                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                                ),
                                                factory.CreateOption("5", "לזימון וביטול תורים לבדיקות מעבדה").AddSubMenu(
                                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                                ),
                                                factory.CreateOption("6", "לזימון וביטול תורים דיאטניות ועובדים סוציאליים").AddSubMenu(
                                                        factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23")
                                                )

                                        ),
                                        factory.CreateOption("2", "מידע על הכנה לבדיקות").AddSubMenu(
                                                factory.CreateOption("1", "לקבלת ססטוס הטיפול"),
                                                factory.CreateOption("2", "נציג")
                                        ),
                                        factory.CreateOption("3", "התייעצות עם אחיות מכבי ללא הפסקה").AddSubMenu(
                                                factory.CreateDataOption("אנא הקישו תעודת זהות", "ID", "%23")
                                        ),
                                        factory.CreateOption("4", "יועצת הנקה").AddSubMenu(
                                                factory.CreateDataOption("אנא הקישו תעודת זהות", "ID", "%23")
                                        ),
                                        factory.CreateOption("5", "מוקד נשים בנושאי הריון,לידה ובריאות האישה").AddSubMenu(
                                                factory.CreateDataOption("אנא הקישו תעודת זהות", "ID", "%23")
                                        ),
                                        factory.CreateOption("6", "מעבר לחברת נטלי להזמנת רופאים לביקור בית"),
                                        factory.CreateOption("8", "ייעוץ רופא לילדים מחוץ לשעות המרפאה").AddSubMenu(
                                                factory.CreateDataOption("אנא הקישו תעודת זהות של ההורה", "ID", "%23").AddSubMenu(
                                                        factory.CreateDataOption("אנא הקישו תעודת זהות של הילד", "ID", "%23")
                                                )
                                        )
                                ),
                        factory.CreateOption("5", "מעבר לחברות הבת:מכבי דנט,מכבי טבעי ובית בלב").AddSubMenu(
                                factory.CreateOption("1", "אסותא"),
                                factory.CreateOption("2", "מכבי דנט"),
                                factory.CreateOption("3", "מכבי טבעי"),
                                factory.CreateOption("4", "בית בלב")

                        ),
                        factory.CreateOption("6", "לחברת נטלי להזמנת רופא לביקור בית"),

                        factory.CreateOption("7", "לרכישת וקבלת מידע על ביטוח חו''ל").ChangePostKeys(",,,,,,").AddSubMenu(
                                factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23").ChangePostKeys("%23,,,,1,").AddSubMenu(
                                        factory.CreateOption("1", "רכישת פוליסה"),
                                        factory.CreateOption("2", "קבלת שירות"),
                                        factory.CreateOption("3", "הגשת תביעה")
                                )
                        ),
                        factory.CreateOption("8", "לאישור קבלת שירות ללא כרטיס מגנטי").AddSubMenu(
                                factory.CreateDataOption("נא הקישו תעודת זהות", "ID", "%23").ChangePostKeys("%23,,,,1,")
                        )
                ).GetOption();
    }

    public Option HotFactory() {
        Factory factory = new Factory();
        Log.e(TAG, "testFactory() <<");
        return factory.CreateOption("*6900", "הוט").ChangePostKeys(",1,")
                .AddSubMenu(
                        factory.CreateOption("1", "מוקד מכירות").AddSubMenu(
                                factory.CreateOption("1", "לקוחות פרטיים").ChangePostKeys(",,%23,").AddSubMenu(
                                        factory.CreateOption("1", "הצטרפות לטריפל או טלוויזיה"),
                                        factory.CreateOption("2", "הצטרפות לאנטרנט ולטלפון")
                                ),
                                factory.CreateOption("5", "לקוחות עסקיים").AddSubMenu(
                                        factory.CreateOption("1", "הצטרפות לשירותי אנטרנט,טלפון,וטלוויזיה לעסקים"),
                                        factory.CreateOption("2", "הצטרפות לשירותי תמסורת,Pri ופתרונות מתקדמים"))
                        ),
                        factory.CreateOption("2", "מוקד מעבר דירה-ללקוחות בלבד").AddSubMenu(
                                factory.CreateOption("1", "קבלת SMS עם קישור למעבר דירה דרך האתר/האפליקציה"),
                                factory.CreateOption("2", "ביצוע מעבר דירה דרך SMS"),
                                factory.CreateOption("3", "נציג שירות")
                        ),
                        factory.CreateOption("3", "מוקד שירות ותמיכה").AddSubMenu(
                                factory.CreateOption("1", "בירור חשבון"),
                                factory.CreateOption("2", "תמיכה טכנית")
                        )).GetOption();
    }

    public Option MeuhedetFactory() {
        Log.e(TAG, "testFactory() >>");

        Factory factory = new Factory();
        Log.e(TAG, "testFactory() <<");
        return factory.CreateOption("*3833", "מאוחדת").ChangePostKeys(",%23,1,")
                .AddSubMenu(
                        factory.CreateOption("1", "קבלת מידע וזימון תור לרופא ושירותי מעבדה")
                                .AddSubMenu(
                                        factory.CreateDataOption("הקש תז", "ID", "%23").ChangePostKeys("%23,,")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "זימון/ביטול תור לרופא משפחה"),
                                                        factory.CreateOption("2", "רופא ילדים"),
                                                        factory.CreateOption("4", "רופא אחר וקבלת מידע")
                                                )
                                ),
                        factory.CreateOption("2", "קבלת מידע וזימון תור לאולטרסאונד/אקו לב/ממוגרפיה/טיפת חלב"),
                        factory.CreateOption("3", "הצטרפות למאוחדת"),
                        factory.CreateOption("4", "מוקד אחיות בנושא היריון/לידה/משכב לידה/הנקה/ילודים").ChangePostKeys(",,,,,")
                                .AddSubMenu(
                                        factory.CreateOption("1", "העברה לנציג")
                                ),
                        factory.CreateOption("5", "לביטוח מאודחת עדיף/שיא/סיעודי,ביטוח לחול ונושאים כספיים")
                                .AddSubMenu(
                                        factory.CreateOption("1", "מידע על ביטוחים משלימים")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש תז", "ID", "%23").ChangePostKeys("%23,")
                                                ),
                                        factory.CreateOption("2", "ביטוח חול")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש תז", "ID", "%23").ChangePostKeys("%23,")
                                                ),
                                        factory.CreateOption("3", "נושאים כספיים")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש תז", "ID", "%23").ChangePostKeys("%23,")
                                                ),
                                        factory.CreateOption("4", "מידע על ביטוח סיעודי")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש תז", "ID", "%23").ChangePostKeys("%23,")
                                                )
                                ),
                        factory.CreateOption("6", "מידע ותמיכה בשירותים הדיגיטליים")
                                .AddSubMenu(
                                        factory.CreateOption("1", "מידע ותמיכה בביקור דיגיטלי")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש תז", "ID", "%23").ChangePostKeys("%23,")
                                                ),
                                        factory.CreateOption("2", "מידע ותמיכה באתר ובאפליקציה")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש תז", "ID", "%23").ChangePostKeys("%23,")
                                                )
                                ),
                        factory.CreateOption("7", "רפואת שיניים")
                                .AddSubMenu(
                                        factory.CreateDataOption("הקש תז", "ID", "%23").ChangePostKeys("%23,")
                                ),
                        factory.CreateOption("8", "קבלת מידע בנושא סדנאות בריאות"),
                        factory.CreateOption("*2", "הצטרפות למאוחדת עדיף/שיא")
                                .AddSubMenu(
                                        factory.CreateOption("1", "לקבלת מידע להצטרפות למאוחדת שיא"),
                                        factory.CreateOption("2", "לקבלת מידע להצטרפות למאוחדת סיעודי")
                                )
                ).GetOption();
    }

    public Option IriyatTA() {
        Factory factory = new Factory();
        return factory.CreateOption("035218666", "עיריית תל אביב-יפו").ChangePostKeys(",,,,,,,")
                .AddSubMenu(
                        factory.CreateOption("1", "מידע ובירורים")
                                .AddSubMenu(
                                        factory.CreateOption("1", "מוקד שירות  בנושא ארנונה וטאבו").
                                                AddSubMenu(
                                                        factory.CreateOption("1", "לארנונה").
                                                                AddSubMenu(
                                                                        factory.CreateDataOption("הקש מספר לקוח", "COSTUMER_NO", "%23,1")
                                                                ),
                                                        factory.CreateOption("2", "טאבו")
                                                ),
                                        factory.CreateOption("2", "מוקד שירות בנושא חנייה ונתיבי תחבורה ציבוריים")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "מידע היכן נגרר רכבך")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("הקש מספר רכב", "CAR_NO", "%23")
                                                                ),
                                                        factory.CreateOption("2", "מידע בנושא תווי חנייה")
                                                ),
                                        factory.CreateOption("3", "מוקד שירות בנושא חינוך")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "מרכז שירות חינוך")
                                                                .AddSubMenu(
                                                                        factory.CreateOption("1", "מידע על גני ילדים"),
                                                                        factory.CreateOption("2", "מידע על בתי ספר")
                                                                ),
                                                        factory.CreateOption("2", "בירורים בנושא תשלומי חינוך")
                                                ),
                                        factory.CreateOption("4", "מוקד שירות בנושאי שילוט , רווחה, דוחות אופניים וחוקי עזר עירוניים")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "נושאי שילוט"),
                                                        factory.CreateOption("2", "נושאי רווחה"),
                                                        factory.CreateOption("3", "דוחות אופניים וחוקי עזר עירוניים (סביבה, חופים ושילוט)")
                                                )
                                ),
                        factory.CreateOption("2", "תשלומים")
                                .AddSubMenu(
                                        factory.CreateOption("1", "תשלומי ארנונה")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "תשלום במענה הקולי")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("הקש מספר חשבון לקוח", "COSTUMER_NO", "%23,1").
                                                                                AddSubMenu(
                                                                                        factory.CreateOption("1", "תשלום שובר").
                                                                                                AddSubMenu(
                                                                                                        factory.CreateDataOption("נא הקישו מספר שובר", "VOUCHER_NO", "%23,1")
                                                                                                ),
                                                                                        factory.CreateOption("2", "תשלום יתרה")
                                                                                )
                                                                ),
                                                        factory.CreateOption("2", "תשלום באמצעות נציג שירות")
                                                ),
                                        factory.CreateOption("2", "תשלומי חנייה")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "תשלום באמצעות נציג שירות"),
                                                        factory.CreateOption("2", "בירורים ותשלומים בנושא דוחות שעבר מועד התשלום")
                                                ),
                                        factory.CreateOption("3", "תשלומי שילוט")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "תשלום באמצעות נציג שירות")
                                                ),
                                        factory.CreateOption("4", "אגרות חינוך, רווחה ותשלומים נוספים")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "תשלומי אגרות ושירותי חינוך")
                                                                .AddSubMenu(
                                                                        factory.CreateOption("1", "תשלום באמצעות נציג שירות")
                                                                ),
                                                        factory.CreateOption("2", "תשלומי רווחה"),
                                                        factory.CreateOption("3", "דוחות אופניים חוקי עזר עירוניים, סביבה חופים ושילוט")
                                                )
                                ),
                        factory.CreateOption("3", "זימון תורים")
                                .AddSubMenu(
                                        factory.CreateOption("1", "זימון תורים למרכז השירות העירוני")
                                ),
                        factory.CreateOption("4", "תמיכה טכנית בשירותי דיגיתל, זימון תורים באתר העירייה").
                                AddSubMenu(
                                        factory.CreateOption("1", "נושאי דיגיתל"),
                                        factory.CreateOption("2", "לזימון תורים")
                                ),
                        factory.CreateOption("6", "תשלום על חנייה ")
                ).GetOption();
    }

    public Option MisradHarishuy() {
        Log.e(TAG, "testFactory() >>");

        Factory factory = new Factory();
        Log.e(TAG, "testFactory() <<");
        return factory.CreateOption("*5678", "משרד הרישוי")
                .AddSubMenu(
                        factory.CreateOption("1", "קבלת שירות באמצעות נציג שירות אנושי").AddPostKeys(",,,,,1,%23,").
                                AddSubMenu(
                                        factory.CreateOption("1", "למעבר לנציג שירות לצורך ביצוע תשלומים"),
                                        factory.CreateOption("2", "למעבר לנציג שירות לצורך קבלת מידע ושירותים נוספים")
                                ),
                        factory.CreateOption("2", "קבלת שירות באמצעות מענה קולי אוטומטי").
                                AddSubMenu(
                                        factory.CreateOption("1", "טיפול ברשיון הנהיגה")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "חידוש רשיון הנהיגה")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("הקש ת.ז", "ID", "%23")
                                                                                .AddSubMenu(
                                                                                        factory.CreateDataOption("הקש מספר רשיון נהיגה", "LICENSE_NO", "%23")
                                                                                )
                                                                ),
                                                        factory.CreateOption("3", "אובדן רשיון הנהיגה")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("הקש ת.ז", "ID", "%23")
                                                                                .AddSubMenu(
                                                                                        factory.CreateDataOption("הקש שנת לידה", "BIRTH_YEAR", "")
                                                                                )
                                                                ),
                                                        factory.CreateOption("4", "תשלום עבור הרישיון הזמני")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("הקש ת.ז", "ID", "%23")
                                                                                .AddSubMenu(
                                                                                        factory.CreateDataOption("הקש מספר רשיון נהיגה", "LICENSE_NO", "%23")
                                                                                )
                                                                )
                                                ),
                                        factory.CreateOption("2", "טיפול ברשיון הרכב")
                                                .AddSubMenu(
                                                        factory.CreateOption("1", "חידוש רשיון הרכב")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("הקש את מספר הרכב", "CAR_NO", "%23")
                                                                                .AddSubMenu(
                                                                                        factory.CreateDataOption("הקש את מספר זהות בעל הרכב/מספר החברה בעלת הרכב", "DRIVER_COMP_NO", "%23")
                                                                                )
                                                                ),
                                                        factory.CreateOption("3", "אובדן רשיון הרכב")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("הקש את מספר הרכב", "CAR_NO", "%23")
                                                                                .AddSubMenu(
                                                                                        factory.CreateDataOption("הקש את מספר זהות בעל הרכב/מספר החברה בעלת הרכב", "DRIVER_COMP_NO", "%23")
                                                                                )
                                                                )
                                                ),
                                        factory.CreateOption("4", "קבלת תוצאות המבחן המעשי (טסט)").ChangePostKeys(",,,,")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש ת.ז", "ID", "%23,")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("תאריך תאוריה: DD/MM/YYYY", "DATE", "")
                                                                )
                                                )
                                )
                ).GetOption();
    }

    public Option MisradHapnim() {
        Factory factory = new Factory();
        return factory.CreateOption("037632666", "משרד הפנים")
                .AddSubMenu(
                        factory.CreateOption("1", "הנהלת מחוז תל אביב")
                                .AddSubMenu(
                                        factory.CreateOption("1", "מרכלית"),
                                        factory.CreateOption("2", "קצין המחוז לשלטון מקומי"),
                                        factory.CreateOption("3", "קצין המחוז לתקציבים"),
                                        factory.CreateOption("4", "ארכיב"),
                                        factory.CreateOption("5", "גזבר"),
                                        factory.CreateOption("6", "הסגן הממונה על המחוז"),
                                        factory.CreateOption("7", "מנהלת המחוז")
                                ),
                        factory.CreateOption("2", "לשכת התכנון")
                                .AddSubMenu(
                                        factory.CreateOption("1", "נושאים כלל מחוזיים, טמעות ופרוגרמות - גברת גל קארו"),
                                        factory.CreateOption("2", "סגנית מתכננת המחוז וראש צוות תל אביב - גברת טלי דותן"),
                                        factory.CreateOption("3", "צוות רמת השרון-הרצליה - גברת דורית רגב"),
                                        factory.CreateOption("4", "צוות רמת גן-גבעתיים - מר שמעון בוחבוט"),
                                        factory.CreateOption("5", "צוות בקעת אונו - גברת אמירה מלמנת"),
                                        factory.CreateOption("6", "מזכירות הועדה המחוזית וועדות המשנה - גברת רחל דוד"),
                                        factory.CreateOption("7", "מזכירת היועץ המשפטי"),
                                        factory.CreateOption("8", "מזכירות מתכננת המחוז - גברת חנית שרעבי"),
                                        factory.CreateOption("9", "צוות התחבורה - גברת רינת קרן")
                                ),
                        factory.CreateOption("3", "יחידת הפיקוח על הבנייה המחוזית"),
                        factory.CreateOption("4", "לשכת מנהל האוכלוסין בנושא אשרות, דרכונים ומרשם")
                                .AddSubMenu(
                                        factory.CreateOption("1", "מחלקת אשרות"),
                                        factory.CreateOption("2", "מחלקת כלי עירייה"),
                                        factory.CreateOption("3", "מחלקת מרשם"),
                                        factory.CreateOption("4", "מחלקת דרכונים"),
                                        factory.CreateOption("5", "ראש ענף אזרחות")
                                )
                ).GetOption();

    }

    public Option BituahLeumi() {
        Factory factory = new Factory();
        return factory.CreateOption("*6050,1", "ביטוח לאומי")
                .AddSubMenu(

                        factory.CreateOption("1", "הזמנת אישורים, קוד סודי וטפסים")
                                .AddSubMenu(
                                        factory.CreateOption("1", "הזמנת אישורים")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש ת.ז", "ID", "%23")
                                                                .AddSubMenu(
                                                                        factory.CreateDataOption("הקש קוד סודי (בן 4 ספרות)", "PRIVATE_CODE", "")
                                                                )
                                                ),
                                        factory.CreateOption("2", "קוד סודי")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש ת.ז", "ID", "%23")
                                                                .AddSubMenu(
                                                                        factory.CreateOption("1", "קבלת מסרון עם קישור להזמנת קוד משתמש לשירות האישי באינטרנט"),
                                                                        factory.CreateOption("2", "להזמנת קוד סודי למוקד הטלפוני ולעמדות השירות העצמי")
                                                                                .AddSubMenu(
                                                                                        factory.CreateOption("1", "קבלת מסרון באמצעות קישור לטלפון"),
                                                                                        factory.CreateOption("2", "קבלת מכתב בדואר עם הקוד")
                                                                                )
                                                                )
                                                ),
                                        factory.CreateOption("3", "הזמנת טפסים")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש ת.ז", "ID", "%23")
                                                                .AddSubMenu(
                                                                        factory.CreateOption("1", "טפסים בנושאי ביטוח וגבייה")
                                                                                .AddSubMenu(
                                                                                        factory.CreateOption("1", "טופס דין וחשבון רב שנתי (6101)"),
                                                                                        factory.CreateOption("2", "טופס בקשה לפתיחת תיק מעסיק במשק בית"),
                                                                                        factory.CreateOption("3", "טופס הצהרת עיסוקים"),
                                                                                        factory.CreateOption("4", "טופס בקשה לעדכון פרטים אישיים")
                                                                                ),
                                                                        factory.CreateOption("2", "טפסים בנושאי נכות ובעיות רפואיות")
                                                                                .AddSubMenu(
                                                                                        factory.CreateOption("1", "נכות כללית")
                                                                                                .AddSubMenu(
                                                                                                        factory.CreateOption("1", "טופס תביעה לקיצבת נכות כללית"),
                                                                                                        factory.CreateOption("2", "טופס בקשה בדיקה חוזרת למקבל קצבת נכות כללית")
                                                                                                ),
                                                                                        factory.CreateOption("2", "שירותים מיוחדים"),
                                                                                        factory.CreateOption("3", "טופס תביעה לקצבת ניידות"),
                                                                                        factory.CreateOption("4", "טופס תביעה לקצבת ילד נכה"),
                                                                                        factory.CreateOption("5", "טופס תביעה לתשלום דמי תאונה אישית"),
                                                                                        factory.CreateOption("6", "טופס בקשה לעדכון פרטים אישיים")
                                                                                )
                                                                )
                                                )
                                ),
                        factory.CreateOption("2", "תשלום בכרטיסי אשראי בנושאי גבייה וגמלאות")
                                .AddSubMenu(
                                        factory.CreateOption("1", "ביצוע תשלומים בנושאי גבייה"),
                                        factory.CreateOption("2", "ביצוע תשלומים בנושאי גמלאות")
                                ),
                        factory.CreateOption("3", "מידע על שעות קבלת קהל בסניפים"),
                        factory.CreateOption("4", "מידע כללי")
                                .AddSubMenu(
                                        factory.CreateOption("1", "מידע על מענק לידה"),
                                        factory.CreateOption("2", "מידע על קצבת ילדים"),
                                        factory.CreateOption("3", "מידע על גיל הפרישה")
                                ),
                        factory.CreateOption("9", "מידע אישי והעברה לנציג")
                                .AddSubMenu(
                                        factory.CreateDataOption("הקש ת.ז", "ID", "%23")
                                                .AddSubMenu(
                                                        factory.CreateDataOption("הקש קוד סודי (בן 4 ספרות)", "PRIVATE_CODE", "")
                                                )
                                )
                ).GetOption();
    }

    public Option EgedFactory() {
        Log.e(TAG, "testFactory() >>");

        Factory factory = new Factory();
        Log.e(TAG, "testFactory() <<");
        return factory.CreateOption("036948888", "אגד").ChangePostKeys(",1,")
                .AddSubMenu(
                        factory.CreateOption("1", "מידע על קווי ושירותי אגד").AddSubMenu(
                                factory.CreateOption("1", "להזמנת כרטיסים לקוי אילת,ים המלח והגליל"),
                                factory.CreateOption("2", "מידע על מסלולים וקוים בינעירוניים").AddSubMenu(
                                        factory.CreateOption("1", "להזנת מספר קו").AddSubMenu(
                                                factory.CreateDataOption("מספר הקו", "NUMBER", "")),
                                        factory.CreateOption("9", "לנציג")
                                ),
                                factory.CreateOption("3", "מידע על מסלולים וקוים עירוניים"),
                                factory.CreateOption("4", "פניות בנושא ביטוח"),
                                factory.CreateOption("5", "להצטרפות לנהגי אגד"),
                                factory.CreateOption("6", "פניות בנושא אבידות"),
                                factory.CreateOption("7", "פניות בנושא רב קו"),
                                factory.CreateOption("8", "פניות הציבור"),
                                factory.CreateOption("9", "לנציג")
                        ),
                        factory.CreateOption("2", "מידע כל כלל החברות - המוקד הארצי")).GetOption();
    }


    public Option PelephoneFactory() {

        //https://firebasestorage.googleapis.com/v0/b/dialrectly.appspot.com/o/Avatars%2FPelephone.png?alt=media&token=5897aa86-880c-4e6d-9ffd-4e415b233c6d
        Log.e(TAG, "testFactory() >>");

        Factory factory = new Factory();
        Log.e(TAG, "testFactory() <<");
        return factory.CreateOption("1800050166", "פלאפון").ChangePostKeys(",,1,")
                .AddSubMenu(
                        factory.CreateOption("1", "שירות עבור מספר זה").AddSubMenu(
                                factory.CreateOption("1", "בירור חשבון"),
                                factory.CreateOption("2", "תמיכה בתקלות"),
                                factory.CreateOption("3", "סיום התקשרות"),
                                factory.CreateOption("4", "מעבר לנציג בנושאים נוספים"),
                                factory.CreateOption("5", "שירות פלאפון חו''ל").AddSubMenu(
                                        factory.CreateOption("1", "בירור חשבון חו''ל"),
                                        factory.CreateOption("2", "תקלות חו''ל"),
                                        factory.CreateOption("3", "נציג")
                                ),
                                factory.CreateOption("6", "הצטרפות לפלאפון או לרכישת מכשיר").ChangePostKeys(",,").AddSubMenu(
                                        factory.CreateOption("1", "הצטרפות,רכישה או שדרוג מכשיר ללקוח פרטי"),
                                        factory.CreateOption("2", "הצטרפות,רכישה או שדרוג מכשיר ללקוח עסקי").AddSubMenu(
                                                factory.CreateOption("1", "תיאום פגישה עם מנהל מכירות בבית העסק"),
                                                factory.CreateOption("2", "מעבר לנציג מכירות")
                                        )

                                ),
                                factory.CreateOption("7", "פעולות בשירות עצמי")
                        ),
                        factory.CreateOption("2", "שירות עבור מספר אחר").AddSubMenu(
                                factory.CreateDataOption("הקלד מספר אחר", "PHONE", "%23").ChangePostKeys(",,,,,,1,")
                                        .AddSubMenu(
                                                factory.CreateOption("1", "בירור חשבון"),
                                                factory.CreateOption("2", "תמיכה בתקלות"),
                                                factory.CreateOption("3", "סיום התקשרות"),
                                                factory.CreateOption("4", "מעבר לנציג בנושאים נוספים"),
                                                factory.CreateOption("5", "שירות פלאפון חו''ל").AddSubMenu(
                                                        factory.CreateOption("1", "בירור חשבון חו''ל"),
                                                        factory.CreateOption("2", "תקלות חו''ל"),
                                                        factory.CreateOption("3", "נציג")
                                                ),
                                                factory.CreateOption("6", "הצטרפות לפלאפון או לרכישת מכשיר").ChangePostKeys(",,").AddSubMenu(
                                                        factory.CreateOption("1", "הצטרפות,רכישה או שדרוג מכשיר ללקוח פרטי"),
                                                        factory.CreateOption("2", "הצטרפות,רכישה או שדרוג מכשיר ללקוח עסקי").AddSubMenu(
                                                                factory.CreateOption("1", "תיאום פגישה עם מנהל מכירות בבית העסק"),
                                                                factory.CreateOption("2", "מעבר לנציג מכירות")
                                                        )

                                                ),
                                                factory.CreateOption("7", "פעולות בשירות עצמי").AddSubMenu(
                                                        factory.CreateOption("1", "בירור מצב חשבון").AddSubMenu(
                                                                factory.CreateOption("1", "עברית"),
                                                                factory.CreateOption("2", "רוסית"),
                                                                factory.CreateOption("3", "ערבית")
                                                        ),
                                                        factory.CreateOption("2", "מכשירכם אבד/נגנב"),
                                                        factory.CreateOption("2", "שעות פעילות במרכזי שירות")
                                                )
                                        )
                        )).GetOption();
    }
}


