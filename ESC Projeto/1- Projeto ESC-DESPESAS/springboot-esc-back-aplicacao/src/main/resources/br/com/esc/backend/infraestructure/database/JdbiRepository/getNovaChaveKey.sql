SELECT
    a.id_ChaveKey,
    (1 + a.id_SequenciaKey) novaChave
FROM
    tbd_ChavePrimaria a
WHERE
    a.tp_RegistroKey = :tpRegistroKey