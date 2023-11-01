UPDATE
    tbd_Parcelas
SET
    tp_Baixado = 'S',
    ds_Observacoes = :dsObservacoes,
    id_DetalheDespesa = :idDetalheDespesa,
    id_Despesa = :idDespesa
WHERE
   id_DespesaParcelada = :idDespesaParcelada
   AND id_Parcelas = :idParcela
   AND id_Funcionario = :idFuncionario
   AND tp_ParcelaAdiada = 'N'