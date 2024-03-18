SELECT
    id_DespesaParcelada,
    id_Parcelas,
    nr_Parcela,
    ds_dataVencimento,
    ds_Observacoes,
    tp_Baixado,
    id_DetalheDespesa,
    id_Despesa,
    tp_Quitado,
    id_Funcionario,
    tp_ParcelaAdiada,
    tp_ParcelaAmortizada,
    vl_Parcela,
    vl_Desconto
FROM
    tbd_Parcelas
WHERE
    id_DespesaParcelada = :idDespesaParcelada
    AND id_Funcionario = :idFuncionario
    AND tp_Baixado = 'N'
    AND tp_ParcelaAdiada = 'N'
    AND tp_ParcelaAmortizada = 'N'
    AND tp_Quitado = 'N'
    AND id_Parcelas NOT IN (SELECT id_Parcela FROM tbd_DetalheDespesasMensais
                          WHERE id_DespesaParcelada = :idDespesaParcelada and id_Funcionario = :idFuncionario)
ORDER BY id_Parcelas ASC