package com.puccampinassi.pi4.t2g09.onconnect.model.reacao;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@DiscriminatorValue("LIKE_COMENTARIO")
public class LikeComentario extends Reacao {}
