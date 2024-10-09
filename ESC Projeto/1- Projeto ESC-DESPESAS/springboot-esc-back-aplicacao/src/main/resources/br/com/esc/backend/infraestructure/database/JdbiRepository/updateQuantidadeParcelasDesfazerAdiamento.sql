UPDATE
    tbd_DespesasParceladas
SET
    nr_ParcelasAdiantadas = (SELECT CASE WHEN MAX(nr_ParcelasAdiantadas) -1 = -1 THEN 0 ELSE (MAX(nr_ParcelasAdiantadas) - 1) END
                            FROM tbd_DespesasParceladas
                            WHERE id_Funcionario = :idFuncionario AND id_DespesaParcelada = :idDespesaParcelada)
WHERE
    id_Funcionario = :idFuncionario
    AND id_DespesaParcelada = :idDespesaParcelada