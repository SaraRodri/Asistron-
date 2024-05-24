package com.example.myapplication
import android.content.Intent
import androidx.activity.ComponentActivity
import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.CheckBox
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import android.widget.Toast
import com.example.myapplication.BackUser
import com.example.myapplication.GoToMenu
import com.example.myapplication.R
import org.json.JSONObject

class RegisterHorarioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crear_horario)
        val buttonGuardar = findViewById<Button>(R.id.crearHorario)
        var helper: RegisterHorarioActivity = RegisterHorarioActivity()
        buttonGuardar.setOnClickListener {
            helper.guardarDatosFirebase()
        }
    }

    fun guardarDatosFirebase(){
        val db = FirebaseDatabase.getInstance()
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
            Log.d("RegisterHorarioActivity", "Horario registrado exitosamente")
            Toast.makeText(applicationContext, "Horario creado correctamente", Toast.LENGTH_LONG).show()
            setContentView(R.layout.inicio)
        }
            .addOnFailureListener { e ->
                Log.e("RegisterHorarioActivity", "Error al registrar horario: $e")
                Toast.makeText(applicationContext, "Fallo al crear el horario, intente de nuevo",
                    Toast.LENGTH_LONG).show()
                setContentView(R.layout.crear_horario)
            }
    }


    /*fun guardarDatosFirebase() {
        val database = FirebaseDatabase.getInstance()
        val referenciaHorarios = database.getReference("Horarios")
        val nombreHorario = findViewById<EditText>(R.id.nombreH).text.toString()
        referenciaHorarios.orderByChild("Horarios ").equalTo(nombreHorario)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val horario = JSONObject()
                    horario.put("Nombre", nombreHorario)
                    horario.put("Dias", dias())
                    horario.put("HoraI", findViewById<EditText>(R.id.horaI).text.toString())
                    horario.put("HoraF", findViewById<EditText>(R.id.horaF).text.toString())
                    referenciaHorarios.push().setValue(horario)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Éxito
                                val intent = Intent(
                                    this@RegisterHorarioActivity,
                                    GoToMenu::class.java
                                ) // Cambia a InicioActivity
                                startActivity(intent)
                                finish()

                                startActivity(intent)
                                finish()
                            } else {
                                // Error
                                val intent = Intent(
                                    this@RegisterHorarioActivity,
                                    BackUser::class.java
                                ) // Cambia a InicioActivity
                                startActivity(intent)
                                finish()

                                startActivity(intent)
                                finish()

                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error de la consulta o proporcionar una implementación vacía
                    Toast.makeText(
                        applicationContext,
                        "Error al consultar la base de datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }*/


    fun dias(): String {
        var dias = ""
        var lun = findViewById<CheckBox>(R.id.Lun)
        var mar = findViewById<CheckBox>(R.id.Mar)
        var mie = findViewById<CheckBox>(R.id.Mie)
        var jue = findViewById<CheckBox>(R.id.Jue)
        var vie = findViewById<CheckBox>(R.id.Vie)
        var sab = findViewById<CheckBox>(R.id.Sab)
        var dom = findViewById<CheckBox>(R.id.Dom)
        if (lun.isChecked) {
            dias += "Lunes, "
        }
        if (mar.isChecked) {
            dias += "Martes, "
        }
        if (mie.isChecked) {
            dias += "Miercoles, "
        }
        if (jue.isChecked) {
            dias += "Jueves, "
        }
        if (vie.isChecked) {
            dias += "Viernes, "
        }
        if (sab.isChecked) {
            dias += "Sabado, "
        }
        if (dom.isChecked) {
            dias += "Domingo"
        }
        return dias
    }
}
