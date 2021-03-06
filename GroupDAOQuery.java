package eetac.upc.eetac.dsa.grouptalk.dao;

/**
 * Created by pauli on 12/04/2016.
 */
public interface GroupDAOQuery {
    public final static String UUID = "select REPLACE(UUID(),'-','')";
    public final static String CREAR_GRUPO = "insert into grupo (id, nombre) values (UNHEX(?), ?)";
    public final static String INGRESAR_GRUPO = "insert into relaciones_grupo (grupoid, userid) values (UNHEX(?), unhex(?))";
    public final static String ABANDONAR_GRUPO = "delete from relaciones_grupo where grupoid=unhex(?) and userid=unhex(?)";
    public final static String OBTENER_GRUPOS = "select hex(g.id)as id, g.nombre from grupo g";
    public final static String OBTENER_NOMBRE_POR_ID = "select  hex(g.id) as id, g.nombre from grupo g where id=unhex(?)";
    public final static String OBTENER_ID_GRUPO_POR_NOMBRE = "select  hex(g.id) as id, g.nombre from grupo g where g.nombre=?";
    public final static String COMPROBAR_USER_ASIGNADO_A_GRUPO = "select hex(g.grupoid) as grupoid from relaciones_grupo g where grupoid=unhex(?) and userid=unhex(?)";
}

}
