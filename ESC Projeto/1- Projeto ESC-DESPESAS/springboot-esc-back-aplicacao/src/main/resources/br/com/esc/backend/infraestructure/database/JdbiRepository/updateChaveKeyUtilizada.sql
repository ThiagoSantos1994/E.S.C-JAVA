UPDATE
   tbd_ChavePrimaria
SET
   id_SequenciaKey = (SELECT (1 + id_SequenciaKey) FROM tbd_ChavePrimaria WHERE id_ChaveKey = :idChaveKey)
WHERE
    id_ChaveKey = :idChaveKey