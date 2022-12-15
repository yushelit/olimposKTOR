import com.olimpos.modelo.*
import java.sql.*
import java.sql.Date
import java.time.LocalDate
import java.util.*

import kotlin.collections.ArrayList


object ConexionEstatica {
    var conexion: Connection? = null
    var sentenciaSQL: Statement? = null
    var registro: ResultSet? = null

    fun abrirConexion(): Int {
        var cod = 0
        try{
            val controlador = "com.mysql.jdbc.Driver"
            val URL_DB = "jdbc:mysql://"+Constantes.servidor+"/"+ Constantes.database

            Class.forName(controlador)
            conexion = DriverManager.getConnection(URL_DB, Constantes.usuario, Constantes.psswd)
            sentenciaSQL = ConexionEstatica.conexion!!.createStatement()
            println("Conexión realizada")

        }catch (e: Exception){
            System.err.println("Exception: "+e.message)
            cod = -1
        }
        return cod;
    }
    fun cerrarConexion(): Int {
        var cod = 0
        try{
            conexion!!.close()
            println("Desconectando de la Base de Datos...")
        }catch(e: SQLException){
            cod=-1
        }
        return cod
    }

    fun login(email:String?, pwd:String?): Log? {
        var u: Log? = null
        try{
            abrirConexion()
            val sentencia = "select nombre, email, password, rol from usuario where email = ? AND password = ?"
            val pstmt = conexion!!.prepareStatement(sentencia)
            pstmt.setString(1, email)
            pstmt.setString(2, pwd)
            registro = pstmt.executeQuery()

            while (registro!!.next()){
                u = Log(
                    registro!!.getString("nombre"),
                    registro!!.getString("email"),
                    registro!!.getString("password"),
                    registro!!.getInt("rol")
                )
            }
        }catch (ex: SQLException){
        }finally {
            cerrarConexion()
        }
        return u
    }


    //Usuarios
    fun obtenerUsuarios(): ArrayList<Usuario> {
        val lu :ArrayList<Usuario> = ArrayList(1)
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM usuario where rol = 1"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            while(ConexionEstatica.registro!!.next()){
                lu.add(
                    Usuario(
                        registro!!.getString("nombre"),
                        registro!!.getString("email"),
                        registro!!.getString("password"),
                        registro!!.getInt("sabiduria"),
                        registro!!.getInt("nobleza"),
                        registro!!.getInt("virtud"),
                        registro!!.getInt("maldad"),
                        registro!!.getInt("audacia"),
                        registro!!.getInt("rol")
                    ))
            }
        }catch (e:SQLException){
        }finally {
            cerrarConexion()
        }
        return lu
    }
    fun obtenerUsuario(email:String?): Usuario? {
        var u: Usuario? = null
        try{
            abrirConexion()
            val sentencia = "select * from usuario where email = ?"
            val preparado = conexion!!.prepareStatement(sentencia)
            preparado.setString(1, email)
            registro = preparado.executeQuery()
            if (registro!!.next()){
                u = Usuario(
                    registro!!.getString("nombre"),
                    registro!!.getString("email"),
                    registro!!.getString("password"),
                    registro!!.getInt("sabiduria"),
                    registro!!.getInt("nobleza"),
                    registro!!.getInt("virtud"),
                    registro!!.getInt("maldad"),
                    registro!!.getInt("audacia"),
                    registro!!.getInt("rol")
                )
            }
        }catch (ex: SQLException){
        }finally {
            cerrarConexion()
        }
        return u
    }
    fun agregarUsuario(usuario: Usuario) :Int {
        var cod = 0
        try {
            abrirConexion()
            val sentencia = "insert into usuario values(?,?,?,?,?,?,?,?,?)"
            val insert = conexion!!.prepareStatement(sentencia)

            insert!!.setString(0,usuario.nombre)
            insert.setString(1,usuario.email)
            insert.setString(2,usuario.password)
            insert.setInt(3,usuario.sabiduria)
            insert.setInt(4,usuario.nobleza)
            insert.setInt(5,usuario.virtud)
            insert.setInt(6,usuario.maldad)
            insert.setInt(7,usuario.audacia)
            insert.setInt(8,usuario.rol)

            insert!!.execute()

        }catch (ex : SQLException){
            cod = -1
        }finally {
            cerrarConexion()
        }
        return cod
    }
    fun modificarUsuario(email: String?, user:Usuario): Int {
        var cod = 0
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM usuario where email = $email"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            if(registro!!.next()){
                val up = "UPDATE usuario SET nombre = ?, password = ? WHERE email = $email"
                val reg = conexion!!.prepareStatement(up)
                reg!!.setString(0,user.nombre)
                reg.setString(1,user.password)
                reg.execute()
            }else{
                cod = -1
            }
        }catch (ex: SQLException){
        }finally {
            cerrarConexion()
        }
        return cod
    }

    //humanos
    fun obtenerHumanos(): ArrayList<Humano>{
        val lh :ArrayList<Humano> = ArrayList(1)
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM humanos"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            while(registro!!.next()){
                var dia:Int = 0
                var mes:Int = 0
                var anio:Int = 0

                var fecha:Date? =  registro!!.getDate("fechaMuerte")
                var fecha2:LocalDate? = fecha?.toLocalDate()
                println(fecha2)

                if(fecha2 != null){
                        dia = fecha2.dayOfMonth
                        mes = fecha2.monthValue
                        anio = fecha2.year
                }
                lh.add(
                    Humano(
                        registro!!.getString("email"),
                        registro!!.getInt("destino"),
                        registro!!.getString("dios"),
                        registro!!.getBoolean("estaVivo"),
                        dia, mes, anio))
            }
        }catch (e:SQLException){
        }finally {
            cerrarConexion()
        }
        return lh
    }
    fun obtenerHumano(email:String?): Humano? {
        var h: Humano? = null
        try {
            abrirConexion()
            val sentencia = "select * from humanos where email = ?"
            val preparado = conexion!!.prepareStatement(sentencia)
            preparado.setString(1, email)
            registro = preparado.executeQuery()

            if (registro!!.next()) {
                var dia:Int = 0
                var mes:Int = 0
                var anio:Int = 0

                val fecha = registro!!.getDate("fechaMuerte")
                val fecha2: LocalDate? = fecha?.toLocalDate()

                if(fecha2 != null){
                    dia = fecha2.dayOfMonth
                    mes = fecha2.monthValue
                    anio = fecha2.year
                }
                h = Humano(
                    registro!!.getString("email"),
                    registro!!.getInt("destino"),
                    registro!!.getString("dios"),
                    registro!!.getBoolean("estaVivo"),
                    dia, mes, anio)
            }
        } catch (ex: SQLException) {
        } finally {
            cerrarConexion()
        }
        return h
    }
    fun agregarHumano(human:Humano):Int {
        var cod = 0
        try{
            abrirConexion()
            val sentencia = "insert into humano values(?,?,?,?,?)"
            val insert = conexion!!.prepareStatement(sentencia)

            var fecha:Date? = null
            if(human.year > 0 && human.mes >0 && human.dia > 0){
                fecha = Date.valueOf(LocalDate.parse(human.year.toString()+"-" +human.mes.toString()+"-"+human.dia.toString()))
            }

            insert.setString(0,human.email)
            insert.setInt(1,human.destino)
            insert.setString(2,human.dios)
            insert.setBoolean(3,human.alive)
            insert.setDate(4, fecha)
            insert!!.execute()
        }catch (ex: SQLException){
            cod = -1
        }finally {
            cerrarConexion()
        }
        return cod;
    }

    fun modificarHumano(email: String?, human: Humano): Int {
        var cod = 0
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM humanos where email = $email"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            if(registro!!.next()){
                val up = "UPDATE humanos SET destino = destino+?, estaVivo = ?, fechaMuerte = ? WHERE email = $email"
                val reg = conexion!!.prepareStatement(up)

                var fecha:Date? = null
                if(human.year!! > 0 && human.mes!! >0 && human.dia!! > 0){
                    fecha = Date.valueOf(LocalDate.parse(human.year.toString()+"-" +human.mes.toString()+"-"+human.dia.toString()))
                }

                reg!!.setInt(0,human.destino)
                reg.setBoolean(1,human.alive)
                reg.setDate(2, fecha)
                reg.execute()

            }else{
                cod = -1
            }
        }catch (ex: SQLException){
            cod = -1
        }finally {
            cerrarConexion()
        }
        return cod
    }

    //Dioses
    fun modificarDios(email: String?,user:Usuario): Int {
        var cod = 0
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM usuario where email = $email"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            if(registro!!.next()){
                val up =
                    "UPDATE usuario SET password = ?, sabiduria = ?, nobleza = ?, virtud = ?, maldad = ?, audacia = ? WHERE email = $email"
                val reg = conexion!!.prepareStatement(up)
                reg!!.setString(0,user.password)
                reg.setInt(1,user.sabiduria)
                reg.setInt(2,user.nobleza)
                reg.setInt(3,user.virtud)
                reg.setInt(4,user.maldad)
                reg.setInt(5,user.audacia)
                reg.execute()
            }else{
                cod = -1
            }
        }catch (ex: SQLException){
        }finally {
            cerrarConexion()
        }
        return cod
    }
    fun obtenerDioses(): ArrayList<Usuario> {
        val lu :ArrayList<Usuario> = ArrayList(1)
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM usuario where rol = 0"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            while(ConexionEstatica.registro!!.next()){
                lu.add(
                    Usuario(
                        registro!!.getString("nombre"),
                        registro!!.getString("email"),
                        registro!!.getString("password"),
                        registro!!.getInt("sabiduria"),
                        registro!!.getInt("nobleza"),
                        registro!!.getInt("virtud"),
                        registro!!.getInt("maldad"),
                        registro!!.getInt("audacia"),
                        registro!!.getInt("rol")
                    ))
            }
        }catch (e:SQLException){
        }finally {
            cerrarConexion()
        }
        return lu
    }
    fun obtenerDios(email: String?): Usuario? {
        val god: Usuario? = null
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM usuario where rol = 0 AND email = $email"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            if(ConexionEstatica.registro!!.next()){
                    Usuario(
                        registro!!.getString("nombre"),
                        registro!!.getString("email"),
                        registro!!.getString("password"),
                        registro!!.getInt("sabiduria"),
                        registro!!.getInt("nobleza"),
                        registro!!.getInt("virtud"),
                        registro!!.getInt("maldad"),
                        registro!!.getInt("audacia"),
                        registro!!.getInt("rol")
                    )
            }
        }catch (e:SQLException){
        }finally {
            cerrarConexion()
        }
        return god
    }

    //Obtencion por afinidad
    fun obtenerAfines(nombre: String): ArrayList<Humano>{
        val lh :ArrayList<Humano> = ArrayList(1)
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM humanos where alive = true AND dios = $nombre"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            while(ConexionEstatica.registro!!.next()){
                var dia:Int = 0
                var mes:Int = 0
                var anio:Int = 0

                val fecha = registro!!.getDate("fechaMuerte")
                val fecha2: LocalDate? = fecha?.toLocalDate()

                if(fecha2 != null){
                    dia = fecha2.dayOfMonth
                    mes = fecha2.monthValue
                    anio = fecha2.year
                }
                lh.add(
                    Humano(
                        registro!!.getString("email"),
                        registro!!.getInt("destino"),
                        registro!!.getString("dios"),
                        registro!!.getBoolean("alive"),
                        dia, mes, anio))
            }
        }catch (e:SQLException){
        }finally {
            cerrarConexion()
        }
        return lh
    }

    //pruebas
    fun obtenerTipos(): ArrayList<String> {
        val lt :ArrayList<String> = ArrayList(1)
        try{
            abrirConexion()
            val sentencia = "SELECT tipo FROM pruebas"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            while(registro!!.next()) {
                lt.add(registro!!.getString("tipo"))
            }
        }catch (ex :SQLException){
        }finally {
            cerrarConexion()
        }
        return lt
    }
    fun obtenerPruebasPorTipo(tipo: String): ArrayList<Pruebas> {
        val lp :ArrayList<Pruebas> = ArrayList(1)
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM pruebas WHERE tipo = $tipo"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            while(registro!!.next()) {
                lp.add(
                    Pruebas(
                        registro!!.getInt("id"),
                        registro!!.getString("tipo"),
                        registro!!.getString("pregunta"),
                        registro!!.getString("atributo"),
                        registro!!.getInt("destino"),
                        registro!!.getString("respCorrecta"),
                    )
                )
            }
        }catch (ex :SQLException){
        }finally {
            cerrarConexion()
        }
        return lp
    }
    fun obtenerPrueba(id: Int): Pruebas? {
        var p :Pruebas? = null
        abrirConexion()
        val sentencia = "select * from pruebas where id = ?"
        val preparado = conexion!!.prepareStatement(sentencia)
        preparado.setInt(1, id)
        registro = preparado.executeQuery()

        if(registro!!.next()){
            p = Pruebas(
                registro!!.getInt("id"),
                registro!!.getString("tipo"),
                registro!!.getString("pregunta"),
                registro!!.getString("atributo"),
                registro!!.getInt("destino"),
                registro!!.getString("respCorrecta"),
            )
        }
        return p
    }

    //registros
    fun obtenerRegistros(mail: String?): ArrayList<DetalleRegistro> {
        val dp: ArrayList<DetalleRegistro> = ArrayList(1)
        abrirConexion()
        val sentencia = "select pruebas.tipo, pruebas.pregunta, pruebas.id, registro.idHumano, registro.respuesta " +
                "from pruebas join registro on pruebas.id=registro.idP where idHumano = ?"
        val preparado = conexion!!.prepareStatement(sentencia)
        preparado.setString(1, mail)
        registro = preparado.executeQuery()
        while(registro!!.next()){
            dp.add(DetalleRegistro(
                registro!!.getInt("id"),
                registro!!.getString("tipo"),
                registro!!.getString("pregunta"),
                registro!!.getString("idHumano"),
                registro!!.getString("respuesta"),
            ))
        }
        return dp
    }
    fun obtenerRegistro(mail:String?, id: Int): DetalleRegistro? {
        var detalle: DetalleRegistro? = null
        abrirConexion()
        val sentencia = "select pruebas.tipo, pruebas.pregunta, pruebas.id, registro.idHumano, registro.respuesta " +
                "from pruebas join registro on pruebas.id=registro.idP where idHumano = ? AND id = ?"
        val preparado = conexion!!.prepareStatement(sentencia)
        preparado.setString(1, mail)
        preparado.setInt(2, id)
        registro = preparado.executeQuery()

        if(registro!!.next()){
            detalle = DetalleRegistro(
                registro!!.getInt("id"),
                registro!!.getString("tipo"),
                registro!!.getString("pregunta"),
                registro!!.getString("idHumano"),
                registro!!.getString("respuesta"),
            )
        }
        return detalle
    }
    fun insertarRegistro(detalle:Registros): Int {
        var cod = 0
        try{
            abrirConexion()
            val sentencia = "insert into registro values(?,?,?)"
            val insert = conexion!!.prepareStatement(sentencia)
            insert.setInt(0,detalle.idP)
            insert.setString(1,detalle.idHumano)
            insert.setString(2,detalle.respuesta)
            insert!!.execute()
        }catch (ex :SQLException){
            cod = -1
        }
        finally {
            cerrarConexion()
        }
        return cod
    }
    fun modificarRegistro(mail:String?, id: Int, detalle:Registros): Int {
        var cod = 0
        try{

            abrirConexion()
            val up = "UPDATE registro SET respuesta = ? WHERE idHumano = $mail AND id = $id"
            val reg = conexion!!.prepareStatement(up)
            reg!!.setString(0,detalle.respuesta)
            reg.execute()

        }catch (ex :SQLException){
            cod = -1
        }
        finally {
            cerrarConexion()
        }
        return cod
    }
}