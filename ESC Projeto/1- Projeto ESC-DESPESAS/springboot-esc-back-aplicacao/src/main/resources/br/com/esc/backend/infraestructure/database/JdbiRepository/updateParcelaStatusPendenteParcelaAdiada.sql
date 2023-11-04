UPDATE
    tbd_Parcelas
SET
    tp_Baixado = 'N',
    tp_Quitado = 'N',
    tp_ParcelaAdiada = 'N',
    ds_Observacoes = '',
    vl_Parcela = :vlParcela,
    id_DetalheDespesa = 0,
    id_Despesa = 0
WHERE
   id_Despesa = :idDespesa
   AND id_DetalheDespesa = :idDetalheDespesa
   AND id_DespesaParcelada = :idDespesaParcelada
   AND id_Parcelas = :idParcela
   AND id_Funcionario = :idFuncionario
   AND tp_ParcelaAdiada = 'S'