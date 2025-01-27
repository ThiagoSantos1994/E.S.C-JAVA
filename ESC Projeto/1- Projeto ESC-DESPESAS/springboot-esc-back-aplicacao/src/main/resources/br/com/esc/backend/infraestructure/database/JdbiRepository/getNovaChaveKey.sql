SELECT
    id_ChaveKey,
    (1 + id_SequenciaKey) novaChave,
    ds_NomeTabela,
    ds_NomeColuna
FROM
    tbd_ChavePrimaria
WHERE
    tp_RegistroKey = :tpRegistroKey