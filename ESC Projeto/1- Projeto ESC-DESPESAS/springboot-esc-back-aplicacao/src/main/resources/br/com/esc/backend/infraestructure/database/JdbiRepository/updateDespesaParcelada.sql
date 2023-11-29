UPDATE
    tbd_DespesasParceladas
SET
    ds_TituloDespesaParcelada = :despesa.dsTituloDespesaParcelada,
    ds_MesVigIni = :despesa.dsMesVigIni,
    ds_AnoVigIni = :despesa.dsAnoVigIni,
    nr_TotalParcelas = :despesa.nrTotalParcelas,
    vl_Fatura = :despesa.vlFatura,
    ds_VigenciaFin = :despesa.dsVigenciaFin
WHERE
    id_DespesaParcelada = :despesa.idDespesaParcelada
    AND id_Funcionario = :despesa.idFuncionario
