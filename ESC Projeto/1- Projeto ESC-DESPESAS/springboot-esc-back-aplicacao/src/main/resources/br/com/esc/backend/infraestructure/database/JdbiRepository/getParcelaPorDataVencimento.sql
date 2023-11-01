SELECT
    id_DespesaParcelada,
    id_Parcelas,
    nr_Parcela,
    id_Despesa,
    id_Funcionario,
    ds_DataVencimento,
    ds_Observacoes,
    id_DetalheDespesa,
    tp_Baixado,
    tp_Quitado,
    tp_ParcelaAdiada,
    tp_ParcelaAmortizada,
    vl_Parcela,
    vl_Desconto
FROM
    tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario
    AND ds_DataVencimento = :dataVencimento