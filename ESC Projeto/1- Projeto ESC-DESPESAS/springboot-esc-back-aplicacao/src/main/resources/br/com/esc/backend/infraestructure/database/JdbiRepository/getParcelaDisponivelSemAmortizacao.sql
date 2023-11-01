SELECT TOP 1
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
    AND tp_Baixado = 'N'
    AND tp_ParcelaAdiada = 'N'
    AND tp_Quitado = 'N'
    AND id_Parcelas NOT IN (SELECT id_Parcela
                            FROM tbd_DetalheDespesasMensais
                            WHERE id_DespesaParcelada = :idDespesaParcelada AND id_Funcionario = :idFuncionario
                            )
ORDER BY
    id_Parcelas