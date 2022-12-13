import com.olimpos.modelo.*
import java.sql.*
import java.time.LocalDate
import java.util.*
import java.util.Date
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

    fun login(email:String?, pwd:String?): Usuario? {
        var u: Usuario? = null
        try{
            abrirConexion()
            val sentencia = "select * from usuario where email = ? AND password = ?"
            val pstmt = conexion!!.prepareStatement(sentencia)
            pstmt.setString(1, email)
            pstmt.setString(2, pwd)
            registro = pstmt.executeQuery()

            while (registro!!.next()){
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
    fun borrarUsuario(email: String?):Int {
        var cod = 0
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM usuario where email = $email"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            if(registro!!.next()){
                val del = "DELETE FROM usuario where email = $email"
                val reg = conexion!!.prepareStatement(del)
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
            val sentencia = "SELECT * FROM humanos where alive = true"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            while(ConexionEstatica.registro!!.next()){
                val fecha = registro!!.getDate("fechaMuerte")
                val dia = fecha.day
                val mes = fecha.month
                val anio = fecha.year
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
    fun obtenerHumano(email:String?): Humano? {
        var h: Humano? = null
        try {
            abrirConexion()
            val sentencia = "select * from usuario where email = ?"
            val preparado = conexion!!.prepareStatement(sentencia)
            preparado.setString(1, email)
            registro = preparado.executeQuery()

            if (registro!!.next()) {
                val fecha = registro!!.getDate("fechaMuerte")
                val dia = fecha.day
                val mes = fecha.month
                val anio = fecha.year
                h = Humano(
                    registro!!.getString("email"),
                    registro!!.getInt("destino"),
                    registro!!.getString("dios"),
                    registro!!.getBoolean("alive"),
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

            val fecha: java.sql.Date = java.sql.Date(human.year, human.mes,human.dia)

            insert.setString(0,human.email)
            insert.setInt(1,human.destino)
            insert.setString(2,human.dios)
            insert.setBoolean(3,human.alive)
            insert.setDate(4, fecha)

        }catch (ex: SQLException){
            cod = -1
        }finally {
            cerrarConexion()
        }
        return cod;
    }
    fun borrarHumano(email: String?) : Int {
        var cod = 0
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM humanos where email = $email"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            if(registro!!.next()){
                val del = "DELETE FROM humanos where email = $email"
                val reg = conexion!!.prepareStatement(del)
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
    fun modificarHumano(email: String?, human: Humano): Int {
        var cod = 0
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM humanos where email = $email"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            if(registro!!.next()){
                val up = "UPDATE humanos SET destino = destino+?, estaVivo = ?, fechaMuerte = ? WHERE email = $email"
                val reg = conexion!!.prepareStatement(up)

                val fecha: java.sql.Date = java.sql.Date(human.year, human.mes,human.dia)

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

}