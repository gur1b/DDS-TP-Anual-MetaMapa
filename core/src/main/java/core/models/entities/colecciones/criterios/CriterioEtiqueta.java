package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;
import javax.persistence.*;
@Entity
@DiscriminatorValue("ETIQUETA")
public class CriterioEtiqueta extends Criterio {
    @ManyToOne
    @JoinColumn(name = "etiqueta_id_etiqueta")
    private Etiqueta etiqueta;

    public Etiqueta getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    public CriterioEtiqueta(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    public CriterioEtiqueta() {
    }
    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getEtiquetas().contains(etiqueta);
    }
}
