UPDATE
    tbd_DespesasParceladas
SET
    nr_ParcelasAdiantadas = (SELECT MAX(nr_ParcelasAdiantadas) + 1
                            FROM tbd_DespesasParceladas
                            WHERE id_Funcionario = :idFuncionario AND id_DespesaParcelada = :idDespesaParcelada)
WHERE
    id_Funcionario = :idFuncionario
    AND id_DespesaParcelada = :idDespesaParcelada