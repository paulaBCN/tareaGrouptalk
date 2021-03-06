package eetac.upc.eetac.dsa.grouptalk.dao;

import eetac.upc.eetac.dsa.grouptalk.entity.Group;
import eetac.upc.eetac.dsa.grouptalk.entity.Tema;
import eetac.upc.eetac.dsa.grouptalk.entity.TemaCollection;
import eetac.upc.eetac.dsa.grouptalk.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pauli on 12/04/2016.
 */
public class temaDAOImpl implements temaDAO {
    @Override
    public Tema crear_tema(String nombreusuario, String nombregrupo, String nombretema, String comentario) throws SQLException,UserNoExisteException, GroupNoExisteException{
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;

        try {
            UserDAOImpl comprobaruser = new UserDAOImpl();
            User user = comprobaruser.obtener_UserByLoginid(nombreusuario);
            if (user == null) throw new UserNoExisteException();

            GroupDAOImpl comprobargrupo = new GroupDAOImpl();
            Group grupo = comprobargrupo.obtener_ID_grupo_por_NOMBRE(nombregrupo);
            if (grupo == null) throw new GroupNoExisteException();

            grupo = comprobargrupo.comprobarUsuarioengrupo(grupo.getId(),user.getId());
            if (grupo == null) throw new RelacionNoExisteException();

            connection = Database.getConnection();
            stmt = connection.prepareStatement(temaDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            connection.setAutoCommit(false);
            stmt.close();
            stmt = connection.prepareStatement(temaDAOQuery.CREAR_TEMA);
            stmt.setString(1, id);
            stmt.setString(2, user.getId());
            stmt.setString(3, grupo.getId());
            stmt.setString(4, nombretema);
            stmt.setString(5, comentario);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw e;
        } catch (RelacionNoExisteException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return obtener_tema_por_id(id);

    }
    @Override
    public boolean eliminar_tema(String id) throws SQLException,TemaIDNoExisteException{
        Connection connection = null;
        PreparedStatement stmt = null;
        try{
            Tema tema = obtener_tema_por_id(id);
            if(tema== null) throw new TemaIDNoExisteException();
            connection = Database.getConnection();
            stmt = connection.prepareStatement(temaDAOQuery.ELIMINAR_TEMA);
            stmt.setString(1, id);
            int rows = stmt.executeUpdate();
            return (rows == 1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
    @Override
    public Tema modificar_comentario(String id,String comentario) throws SQLException,TemaIDNoExisteException{
        Connection connection = null;
        PreparedStatement stmt = null;
        Tema tema = null;
        try
        {
            tema = obtener_tema_por_id(id);
            if (tema == null) throw new TemaIDNoExisteException();
            connection = Database.getConnection();
            stmt = connection.prepareStatement(temaDAOQuery.MODIFICAR_COMENTARIO_TEMA);
            stmt.setString(1, comentario);
            stmt.setString(2, id);
            int rows = stmt.executeUpdate();
            if (rows == 1) {tema = obtener_tema_por_id(id);}
        }
        catch (SQLException e) {throw e;}
        finally
        {
            if (stmt != null) stmt.close();
            if (connection != null)
            {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return tema;

    }
    @Override
    public TemaCollection obtener_coleccion_temas_por_nombreGroup(String nombreGroup)throws SQLException, GroupNoExisteException{
        Tema tema = null;
        TemaCollection temcol = new TemaCollection();
        Connection connection = null;
        PreparedStatement stmt = null;
        try{
            GroupDAOImpl comprobargroupìd= new GroupDAOImpl();
            Group grupo = comprobargroupìd.obtener_ID_grupo_por_NOMBRE(nombreGroup);
            if (grupo == null) throw  new GroupNoExisteException();
            connection = Database.getConnection();
            stmt = connection.prepareStatement(temaDAOQuery.OBTENER_COLECCION_TEMAS_POR_GROUPID);
            stmt.setString(1, grupo.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                tema = new Tema();
                tema.setId(rs.getString("id"));
                tema.setUserid(rs.getString("userid"));
                tema.setGroupid(rs.getString("grupoid"));
                tema.setNombre(rs.getString("nombre"));
                tema.setComentario(rs.getString("comentario"));
                temcol.getTemas().add(tema);
            }
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return  temcol;
    }
    @Override
    public Tema obtener_tema_por_id(String id)throws SQLException{
        Tema tema= null;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(temaDAOQuery.OBTENER_TEMA_POR_ID);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                tema = new Tema();
                tema.setId(rs.getString("id"));
                tema.setUserid(rs.getString("userid"));
                tema.setGroupid(rs.getString("grupoid"));
                tema.setNombre(rs.getString("nombre"));
                tema.setComentario(rs.getString("comentario"));

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return tema;
    }
    @Override
    public Tema obtener_id_por_nombreTema(String nombreTema)throws SQLException{
        Tema tema= null;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(temaDAOQuery.OBTENER_ID_POR_NOMBRETEMA);
            stmt.setString(1, nombreTema);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                tema = new Tema();
                tema.setId(rs.getString("id"));
                tema.setUserid(rs.getString("userid"));
                tema.setGroupid(rs.getString("grupoid"));
                tema.setNombre(rs.getString("nombre"));
                tema.setComentario(rs.getString("comentario"));

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return tema;

    }

}
