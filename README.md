# markap-back-apps-int

erDiagram
    USER ||--|| USER-PROFILE : tiene
    USER ||--|{ USER-ROLES : posee
    ROLES ||--|{ USER-ROLES : posee
    ROLES ||--|{ ROLES-PERMISSION : posee
    PERMISSION ||--|{ ROLES-PERMISSION : posee



    USER ||--|| LIKE : likea
    LIKE ||--|| PRODUCTO : esLikeado
    USER ||--|{ VISITADO : visita
    VISITADO }|--|| PRODUCTO : esVisitado
    USER ||--|| CARRITO : posee
    CARRITO ||--o{ ITEM_CARRITO : contiene
    PRODUCTO ||--|{ ITEM_CARRITO : asociado
    PRODUCTO }|--|| CATEGORIA : posee 

    USER {
        int idUsuario
        string username
        string password
        isEnabled bool
        accountNoLocked bool
        accountNoExpired bool
        credentialNoExpired bool
    }

    USER-ROLES {
        int idUserRole
        int idUsuario
        int idRol
    }

    ROLES {
        int idRol
        string roleName
    }

    ROLES-PERMISSION{
        int idRolePermission
        int idPermission
        int idRol
    }

    PERMISSION{
        int idPermission
        string permissionName
    }

    VISITADO {
        int idVisitado
        int idUsuario
        int idProducto
    }

    LIKE {
        int idLike
        int idUsuario
        int idProducto
    }

    CARRITO {
        int idUser
        boolean paymentStatus
    }

    ITEM_CARRITO {
        int idItemCarrito
        int idCarrito
        int idProducto
        int cantidad
    }

    PRODUCTO {
        int idProducto
        BASE64 imagen
        descripcion string
        adicional string
        price precio
        int stock
        int idCategoria
    }

    CATEGORIA {
        int idCategoria
        string nombre
    }

    USER-PROFILE {
        int idUsuario
        string email
        string nombre
        string apellido
        DATE fechaNacimiento
    }
