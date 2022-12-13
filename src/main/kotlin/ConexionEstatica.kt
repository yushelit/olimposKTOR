import com.olimpos.modelo.*
import java.sql.*
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
    fun agregarUsuario(usuario: Usuario) :String {
        try {
            abrirConexion()
            val sentencia = "insert into usuario values(?,?,?,?,?,?,?,?,?)"
            val insert = conexion!!.prepareStatement(sentencia)

            insert!!.setString(1,usuario.nombre)
            insert.setString(2,usuario.email)
            insert.setString(3,usuario.password)
            insert.setInt(4,usuario.sabiduria)
            insert.setInt(5,usuario.nobleza)
            insert.setInt(6,usuario.virtud)
            insert.setInt(7,usuario.maldad)
            insert.setInt(8,usuario.audacia)
            insert.setInt(9,usuario.rol)

            insert!!.execute()

        }catch (ex : SQLException){
        }finally {
            cerrarConexion()
        }
        return "Usuario agregado"
    }
    fun borrarUsuario(email: String?):Int {
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
    fun modificarUsuario(email: String?, user:Usuario): Int {
        var cod = 0
        try{
            abrirConexion()
            val sentencia = "SELECT * FROM humanos where email = $email"
            registro = sentenciaSQL!!.executeQuery(sentencia)
            if(registro!!.next()){
                val up = "UPDATE usuario SET nombre = ?, password = ?"
                val reg = conexion!!.prepareStatement(up)
                reg!!.setString(1,user.nombre)
                reg.setString(2,user.password)
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
                lh.add(
                    Humano(
                        registro!!.getString("email"),
                        registro!!.getInt("destino"),
                        registro!!.getString("dios"),
                        registro!!.getBoolean("alive"),
                        registro!!.getInt("dia"),
                        registro!!.getInt("mes"),
                        registro!!.getInt("year")
                    ))
            }
        }catch (e:SQLException){
        }finally {
            cerrarConexion()
        }
        return lh
    }
    fun obtenerHumano(email:String?): Humano? {
        var h: Humano? = null
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