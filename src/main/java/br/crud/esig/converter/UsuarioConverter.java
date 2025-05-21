package br.crud.esig.converter;

import br.crud.esig.dao.UsuarioDao;
import br.crud.esig.model.Usuario;
import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Usuario.class, value = "usuarioConverter")
public class UsuarioConverter implements Converter<Usuario> {

    @Override
    public Usuario getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        UsuarioDao usuarioDao = CDI.current().select(UsuarioDao.class).get();
        return usuarioDao.buscarPorId(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Usuario value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString();
    }
}