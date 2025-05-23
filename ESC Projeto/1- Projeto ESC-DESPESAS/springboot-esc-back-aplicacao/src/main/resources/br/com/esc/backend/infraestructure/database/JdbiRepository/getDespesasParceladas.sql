SELECT
    id_DespesaParcelada,
    ds_TituloDespesaParcelada,
    ds_MesVigIni,
    ds_AnoVigIni,
    nr_TotalParcelas,
    ds_VigenciaFin,
    vl_Fatura,
    id_Funcionario,
    nr_ParcelasAdiantadas,
    tp_Baixado,
    dt_Cadastro
FROM
    tbd_DespesasParceladas
WHERE
    id_Funcionario = :idFuncionario
    AND <status>
ORDER BY
    ds_TituloDespesaParcelada