package com.example.myapplication.com.example.myapplication.modelo

import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.ComponentActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class RegistrarHorarioActivity : ComponentActivity() {
    private val db = FirebaseDatabase.getInstance()


    fun registrarHorario(){
        val referencia = db.reference
        val nombreHorario = findViewById<EditText>(R.id.nombreH).text.toString()
        val dias = dias()
        val horaI = findViewById<EditText>(R.id.horaI).text.toString()
        val horaF = findViewById<EditText>(R.id.horaF).text.toString()

        val horarioRef = referencia.child("Horarios").push()

        val mapa = mapOf(
            "nombre" to nombreHorario,
            "dias" to dias,
            "horaI" to horaI,
            "horaF" to horaF
        )

        horarioRef.setValue(mapa).addOnSuccessListener {
            Log.d("FirebaseHelper", "Horario registrado exitosamente")
        }
            .addOnFailureListener { e ->
                Log.e("FirebaseHelper", "Error al registrar horario: $e")
            }
    }

    fun dias():String{
        var dias = ""
        var lun = findViewById<CheckBox>(R.id.Lun)
        var mar = findViewById<CheckBox>(R.id.Mar)
        var mie = findViewById<CheckBox>(R.id.Mie)
        var jue = findViewById<CheckBox>(R.id.Jue)
        var vie = findViewById<CheckBox>(R.id.Vie)
        var sab = findViewById<CheckBox>(R.id.Sab)
        var dom = findViewById<CheckBox>(R.id.Dom)
        if (lun.isChecked){dias += "Lunes, "}
        if (mar.isChecked){dias += "Martes, "}
        if (mie.isChecked){dias += "Miercoles, "}
        if (jue.isChecked){dias += "Jueves, "}
        if (vie.isChecked){dias += "Viernes, "}
        if (sab.isChecked){dias += "Sabado, "}
        if (dom.isChecked){dias += "Domingo"}
        return dias
    }
}

