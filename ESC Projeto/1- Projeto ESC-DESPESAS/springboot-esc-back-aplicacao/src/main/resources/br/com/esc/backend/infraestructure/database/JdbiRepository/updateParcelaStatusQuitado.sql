UPDATE
    tbd_Parcelas
SET
    vl_Parcela = :valorQuitacao,
    tp_Baixado = 'S',
    tp_Quitado = 'S',
    ds_Observacoes = :dsObservacoes
WHERE
   id_DespesaParcelada = :idDespesaParcelada
   AND id_Parcelas = :idParcela
   AND id_Funcionario = :idFuncionario
   AND tp_ParcelaAdiada = 'N'
   AND tp_Baixado = 'N'