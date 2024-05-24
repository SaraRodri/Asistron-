package com.example.myapplication

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : ComponentActivity() {
    val db = FirebaseDatabase.getInstance()
    val referencia = db.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)
    }

    fun editarHorario(view: View) {
        val buscando: String = "h"
        setContentView(R.layout.editar_horario)

        val nombreHorarioEditText = findViewById<EditText>(R.id.nombreHE)
        val lunChbx = findViewById<CheckBox>(R.id.LunE)
        val marChbx = findViewById<CheckBox>(R.id.MarE)
        val mieChbx = findViewById<CheckBox>(R.id.MieE)
        val jueChbx = findViewById<CheckBox>(R.id.JueE)
        val vieChbx = findViewById<CheckBox>(R.id.VieE)
        val sabChbx = findViewById<CheckBox>(R.id.SabE)
        val domChbx = findViewById<CheckBox>(R.id.DomE)
        val horaIEditText = findViewById<EditText>(R.id.horaIHE)
        val horaFEditText = findViewById<EditText>(R.id.horaFHE)
        val boton = findViewById<Button>(R.id.editarHorario)
        val bd = db.getReference("Horarios")

        // Verificar si el usuario existe en la base de datos
        bd.orderByChild("nombre").equalTo(buscando).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (horarioSnapshot in snapshot.children) {
                        val nombre = horarioSnapshot.child("nombre").getValue(String::class.java)
                        val dias = horarioSnapshot.child("dias").getValue(String::class.java)
                        val horaI = horarioSnapshot.child("horaI").getValue(String::class.java)
                        val horaF = horarioSnapshot.child("horaF").getValue(String::class.java)

                        // Mostrar los datos en los TextView correspondientes

                        nombreHorarioEditText.setText(nombre)
                        horaIEditText.setText(horaI)
                        horaFEditText.setText(horaF)
                        if (dias!!.contains("Lunes")){lunChbx.isChecked= true}
                        if (dias.contains("Martes")){marChbx.isChecked= true}
                        if (dias.contains("Miercoles")){mieChbx.isChecked= true}
                        if (dias.contains("Jueves")){jueChbx.isChecked= true}
                        if (dias.contains("Viernes")){vieChbx.isChecked= true}
                        if (dias.contains("Sabado")){sabChbx.isChecked= true}
                        if (dias.contains("Domingo")){domChbx.isChecked= true}
                    }
                } else {
                    // El usuario no existe
                    Toast.makeText(applicationContext, "El horario no existe", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de la consulta
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })
        boton.setOnClickListener {
            val nombreHorario = findViewById<EditText>(R.id.nombreHE).text.toString()
            val dias = diasE()
            val horaI = findViewById<EditText>(R.id.horaIHE).text.toString()
            val horaF = findViewById<EditText>(R.id.horaFHE).text.toString()

            val horarioRef = referencia.child("Horarios").push()

            val mapa = mapOf(
                "nombre" to nombreHorario,
                "dias" to dias,
                "horaI" to horaI,
                "horaF" to horaF
            )
            horarioRef.updateChildren(mapa).addOnSuccessListener {
                Log.d("RegisterHorarioActivity", "Horario actualizado exitosamente")
                Toast.makeText(applicationContext, "Horario actualizado correctamente", Toast.LENGTH_LONG).show()
                setContentView(R.layout.inicio)
            }
                .addOnFailureListener { e ->
                    Log.e("RegisterHorarioActivity", "Error al actualizar horario: $e")
                    Toast.makeText(applicationContext, "Fallo al actualizar el horario, intente de nuevo",
                        Toast.LENGTH_LONG).show()
                    setContentView(R.layout.editar_horario)
                }
        }

    }

    fun verHorario(view: View){
        val diasTxt = findViewById<TextView>(R.id.dias)
        val horasTxt = findViewById<TextView>(R.id.horas)
        val buscarTxt = findViewById<EditText>(R.id.buscando)
        val buscar = buscarTxt.text.toString()
        buscarTxt.setText(buscar)

        val bd = db.getReference("Horarios")
        bd.orderByChild("nombre").equalTo(buscar).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (horarioSnapshot in snapshot.children) {
                        var dias = horarioSnapshot.child("dias").getValue(String::class.java)
                        var horaI = horarioSnapshot.child("horaI").getValue(String::class.java)
                        var horaF = horarioSnapshot.child("horaF").getValue(String::class.java)
                        val horas = "Hora: " + horaI+ " a " + horaF

                        // Mostrar los datos en los TextView correspondientes

                        diasTxt.setText(dias)
                        horasTxt.setText(horas)

                    }
                } else {
                    // El usuario no existe
                    Toast.makeText(applicationContext, "El horario no existe", Toast.LENGTH_SHORT)
                        .show()
                    diasTxt.setText("Horario no encontrado")
                    horasTxt.setText("Vuelva a intentar")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        }    )
    }
    fun buscarGrupo(view: View){
        val entrenadortxt = findViewById<TextView>(R.id.entrenadorGV)
        val horariotxt = findViewById<TextView>(R.id.horarioGV)
        val lugartxt = findViewById<TextView>(R.id.lugarGV)
        val buscarTxt = findViewById<EditText>(R.id.buscar)
        val buscar = buscarTxt.text.toString()
        buscarTxt.setText(buscar)

        val bd = db.getReference("Grupos")
        bd.orderByChild("nombreGrupo").equalTo(buscar).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (grupoSnapshot in snapshot.children) {
                        val entrenador = grupoSnapshot.child("entrenador").getValue(String::class.java)
                        val horario = grupoSnapshot.child("horario").getValue(String::class.java)
                        val lugar = grupoSnapshot.child("lugar").getValue(String::class.java)

                        // Mostrar los datos en los TextView correspondientes

                        entrenadortxt.setText(entrenador)
                        horariotxt.setText(horario)
                        lugartxt.setText(lugar)

                    }
                } else {
                    // El usuario no existe
                    Toast.makeText(applicationContext, "El horario no existe", Toast.LENGTH_SHORT)
                        .show()
                    entrenadortxt.setText("Grupo no encontrado")
                    horariotxt.setText("Vuelva a intentar")
                    lugartxt.setText("")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        }    )
    }
    fun diasE():String{
        var dias = ""
        var lun = findViewById<CheckBox>(R.id.LunE)
        var mar = findViewById<CheckBox>(R.id.MarE)
        var mie = findViewById<CheckBox>(R.id.MieE)
        var jue = findViewById<CheckBox>(R.id.JueE)
        var vie = findViewById<CheckBox>(R.id.VieE)
        var sab = findViewById<CheckBox>(R.id.SabE)
        var dom = findViewById<CheckBox>(R.id.DomE)
        if (lun.isChecked){dias += "Lunes, "}
        if (mar.isChecked){dias += "Martes, "}
        if (mie.isChecked){dias += "Miercoles, "}
        if (jue.isChecked){dias += "Jueves, "}
        if (vie.isChecked){dias += "Viernes, "}
        if (sab.isChecked){dias += "Sabado, "}
        if (dom.isChecked){dias += "Domingo"}
        return dias
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

    fun crearHorario(view: View){
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
    fun crearGrupo(view:View){
        val nombreGrupo = findViewById<EditText>(R.id.nombreG).text.toString()
        val entrenador = findViewById<EditText>(R.id.entrenador).text.toString()
        val horarioG = findViewById<EditText>(R.id.horarioG).text.toString()
        val lugar = findViewById<EditText>(R.id.lugarG).text.toString()
        val integrantes = findViewById<EditText>(R.id.spinner_integrantes).text.toString()
        val grupoRef = referencia.child("Grupos").push()
        val mapita = mapOf(
            "nombreGrupo" to nombreGrupo,
            "entrenador" to entrenador,
            "horarioG" to horarioG,
            "lugar" to lugar,
            "integrantes" to integrantes
        )

        grupoRef.setValue(mapita).addOnSuccessListener {
            Log.d("", "Grupo registrado exitosamente")
            Toast.makeText(applicationContext, "Grupo creado correctamente", Toast.LENGTH_LONG).show()
            setContentView(R.layout.inicio)
        }
            .addOnFailureListener { e ->
                Log.e("", "Error al registrar el grupo: $e")
                Toast.makeText(applicationContext, "Fallo al crear el grupo, intente de nuevo",
                    Toast.LENGTH_LONG).show()
                setContentView(R.layout.nuevo_grupo)
            }}

    val positiveButtonClickCrearGrup = { dialog: DialogInterface, which: Int ->
        val h = MainActivity()
        //h.crearGrupo()
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_grupo)
    }

    fun Buscar_Perfil(view: View){
        setContentView(R.layout.buscar_usuario)
    }
    // Dany

    fun Inicio_noti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_notificaciones)
    }

    fun Noti_vernoti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.ver_notificaciones)
    }

    fun Vernoti_torneo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.ver_torneo_mariposa)
    }
    fun Noti_modnoti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.modificar_notificaciones)
    }
    fun Modnoti_torneo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.editar_torneo_mariposa)
    }
    fun Noti_añnoti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.crear_notificacion)
    }
    fun Volvermenu_noti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_notificaciones)
    }
    fun Volvermenu_inicio(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.inicio)
    }
    fun Asist_verasist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.ver_asistencias)
    }
    fun Verasist_aprendiendover(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_ver)
    }
    fun Aprendiendover_aprendiendovermartes09(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_ver_martes09)
    }
    fun Asist_modasist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.modificar_asistencias)
    }
    fun Modasist_modaprendiendo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_modificar)
    }
    fun Modaprendiendo_modaprendiendomartes(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_modificar_martes09)
    }
    fun Asist_añasist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.crear_asistencia)
    }
    fun Añasist_añaprendiendo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_crear)
    }
    fun Volvermenu_asist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_asistencia)
    }
    fun Inicio_asist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_asistencia)
    }


    // Vale

    fun volverButtonClicked(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_grupo)
    }
    fun boton_busqueda(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.buscar_grupo)
    }
    fun Añadir_Grupo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.nuevo_grupo)
    }
    fun boton_modificar_grup(view: View){
        setContentView(R.layout.editar_grupo_activo)
        val spinnerEstado = findViewById<Spinner>(R.id.estado)
        val option_Estado = arrayOf("Activo", "Inactivo")
        spinnerEstado.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, option_Estado)
    }
    fun Buscar_Grupo(view: View){
        setContentView(R.layout.buscar_grupo)
    }
    fun Ver_Grupo(view: View){
        setContentView(R.layout.ver_grupo)
    }
    fun Cambiara_activo(view: View){
        setContentView(R.layout.editar_grupo_inactivo)
    }

    // Saris

    fun horariom_horariov(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.buscar_horario)
    }

    fun horariom_horarioe(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.editar_horario)
    }

    fun Volvermenu_h(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.horario_menu)
    }

    fun Ver_semillero(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.semillero_ejemplo)
    }

    fun registroU(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.registro_usuario)
    }

    fun registroC(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.registro_clave)
    }

    fun volvermU(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.usuario_menu)
    }


    fun usuariom_usuariov(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.ver_perfil)
    }

    fun usuariom_usuarioe(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.registro_usuario)
    }

    fun horariom_horarioc(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.crear_horario)
    }

    //

    fun EditarUsuario(view: View){
        setContentView(R.layout.editar_usuario)
    }

    fun Inicio_grupo(view: View){
        setContentView(R.layout.menu_grupo)
    }
    fun Asist_reporte(view: View){
        setContentView(R.layout.generar_repo)
    }
    fun Inicio_usuarios(view: View){
        setContentView(R.layout.usuario_menu)
    }
    fun Inicio_horarios(view: View){
        setContentView(R.layout.horario_menu)
    }
    fun Inicio_miperfil(view: View){
        setContentView(R.layout.ver_perfil)
    }
    fun Inicio_iniciarsesion(view: View){
        setContentView(R.layout.iniciar_sesion)
    }
    fun Iniciarsesion_inicio(view: View){
        setContentView(R.layout.inicio)
    }
    fun Presentacion_iniciarsesion(view: View){
        setContentView(R.layout.iniciar_sesion)
    }
    fun Registroclave_iniciosesion(view: View){
        setContentView(R.layout.iniciar_sesion)
    }
    fun Buscaruser_buscandousuario(view: View){
        setContentView(R.layout.buscando_usuario)
    }
    fun horarioc_volver(view: View){
        setContentView(R.layout.horario_menu)
    }

    //Pop ups

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
    }
    val neutralButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            "Maybe", Toast.LENGTH_SHORT).show()
    }

    val positiveButtonClickCrearAsist = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_asistencia)

    }

    fun Registrarasist(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_registrarasist, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickCrearAsist))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickModAsist = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_asistencia)
    }

    fun Modasist(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_modasist, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickModAsist))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickCrearNot = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_notificaciones)
    }

    fun Crearnoti(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_crearnoti, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickCrearNot))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickGuardarNot = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_notificaciones)
    }

    fun Guardarnoti(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_guardarnoti, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickGuardarNot))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickModGrup = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_grupo)
    }

    fun Modificargrupo(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_modificargrupo, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickModGrup))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickCrearCuenta = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.iniciar_sesion)
    }

    fun Crearcuenta(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_crearcuenta, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickCrearCuenta))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickEditarUser = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.usuario_menu)
    }

    fun Editarusuario(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_edituser, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickEditarUser))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }


}