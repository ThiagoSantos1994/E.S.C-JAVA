INSERT INTO tbd_DespesasParceladas
    (
        id_DespesaParcelada,
        ds_TituloDespesaParcelada,
        ds_MesVigIni,
        ds_AnoVigIni,
        nr_TotalParcelas,
        vl_Fatura,
        ds_VigenciaFin,
        id_Funcionario,
        nr_ParcelasAdiantadas,
        tp_Baixado,
        dt_Cadastro
    )
VALUES (
    :despesa.idDespesaParcelada,
    :despesa.dsTituloDespesaParcelada,
    :despesa.dsMesVigIni,
    :despesa.dsAnoVigIni,
    :despesa.nrTotalParcelas,
    :despesa.vlFatura,
    :despesa.dsVigenciaFin,
    :despesa.idFuncionario,
    0,
    :despesa.tpBaixado,
    :despesa.dtCadastro
)
