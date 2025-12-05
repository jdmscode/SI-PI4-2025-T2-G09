package com.puccampinassi.pi4.t2g09.onconnect.model.reacao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@DiscriminatorValue("DESLIKE_COMENTARIO")
public class DeslikeComentario extends Reacao {}